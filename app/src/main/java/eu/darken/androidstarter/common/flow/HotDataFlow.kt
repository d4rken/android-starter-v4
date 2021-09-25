package eu.darken.androidstarter.common.flow

import eu.darken.androidstarter.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.androidstarter.common.debug.logging.asLog
import eu.darken.androidstarter.common.debug.logging.log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

/**
 * A thread safe flow that can be updated blocking and async, with way to provide an initial (lazy) value.
 *
 * @param loggingTag will be prepended to logging tag, i.e. "$loggingTag:HD"
 * @param scope on which the update operations and callbacks will be executed on
 * @param coroutineContext used in combination with [CoroutineScope]
 * @param sharingBehavior see [Flow.shareIn]
 * @param startValueProvider provides the first value, errors will be rethrown on [CoroutineScope]
 */
class HotDataFlow<T : Any>(
    loggingTag: String? = null,
    scope: CoroutineScope,
    coroutineContext: CoroutineContext = scope.coroutineContext,
    sharingBehavior: SharingStarted = SharingStarted.WhileSubscribed(),
    private val startValueProvider: suspend CoroutineScope.() -> T
) {
    private val tag = loggingTag?.let { "$it:HDF" }

    init {
        if (tag != null) log(tag, VERBOSE) { "init()" }
    }

    private val updateActions = MutableSharedFlow<Update<T>>(
        replay = Int.MAX_VALUE,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    private val valueGuard = Mutex()

    private val internalProducer: Flow<State<T>> = channelFlow {
        var currentValue = valueGuard.withLock {
            if (tag != null) log(tag, VERBOSE) { "Providing startValue..." }
            startValueProvider().also {
                if (tag != null) log(tag, VERBOSE) { "...startValue provided, emitting $it" }
                val initializer = Update<T>(onError = null, onModify = { it })

                send(State(value = it, updatedBy = initializer))
            }
        }

        if (tag != null) log(tag, VERBOSE) { "...startValue provided and emitted." }

        updateActions
            .onCompletion {
                if (tag != null) log(tag, VERBOSE) { "updateActions onCompletion -> resetReplayCache()" }
                updateActions.resetReplayCache()
            }
            .collect { update ->
                currentValue = valueGuard.withLock {
                    try {
                        update.onModify(currentValue).also {
                            send(State(value = it, updatedBy = update))
                        }
                    } catch (e: Exception) {
                        if (tag != null) log(tag, VERBOSE) {
                            "Data modifying failed (onError=${update.onError}): ${e.asLog()}"
                        }

                        if (update.onError != null) {
                            update.onError.invoke(e)
                        } else {
                            send(State(value = currentValue, error = e, updatedBy = update))
                        }
                        currentValue
                    }
                }
            }

        if (tag != null) log(tag, VERBOSE) { "internal channelFlow finished." }
    }

    private val internalFlow = internalProducer
        .onStart { if (tag != null) log(tag, VERBOSE) { "Internal onStart" } }
        .onCompletion { err ->
            when {
                err is CancellationException -> if (tag != null) log(
                    tag,
                    VERBOSE
                ) { "internal onCompletion() due to cancellation" }
                err != null -> if (tag != null) log(tag, VERBOSE) {
                    "internal onCompletion() due to error: ${err.asLog()}"
                }
                else -> if (tag != null) log(tag, VERBOSE) { "internal onCompletion()" }
            }
        }
        .shareIn(
            scope = scope + coroutineContext,
            replay = 1,
            started = sharingBehavior
        )

    val data: Flow<T> = internalFlow
        .map { it.value }
        .distinctUntilChanged()

    /**
     * Non blocking update method.
     * Gets executed on the scope and context this instance was initialized with.
     *
     * @param onError if you don't provide this, and exception in [onUpdate] will the scope passed to this class
     */
    fun updateAsync(
        onError: (suspend (Exception) -> Unit) = { throw it },
        onUpdate: suspend T.() -> T,
    ) {
        val update: Update<T> = Update(
            onModify = onUpdate,
            onError = onError
        )
        runBlocking { updateActions.emit(update) }
    }

    /**
     * Blocking update method
     * Gets executed on the scope and context this instance was initialized with.
     * Waiting will happen on the callers scope.
     *
     * Any errors that occurred during [action] will be rethrown by this method.
     */
    suspend fun updateBlocking(action: suspend T.() -> T): T {
        val update: Update<T> = Update(onModify = action)
        updateActions.emit(update)

        if (tag != null) log(tag, VERBOSE) { "Waiting for update." }
        val ourUpdate = internalFlow.first { it.updatedBy == update }

        ourUpdate.error?.let { throw it }

        return ourUpdate.value
    }

    private data class Update<T>(
        val onModify: suspend T.() -> T,
        val onError: (suspend (Exception) -> Unit)? = null,
    )

    private data class State<T>(
        val value: T,
        val error: Exception? = null,
        val updatedBy: Update<T>,
    )
}
