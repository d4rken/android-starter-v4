package eu.darken.androidstarter.common.ui2

import androidx.navigation.NavDirections
import eu.darken.androidstarter.common.coroutine.DispatcherProvider
import eu.darken.androidstarter.common.debug.logging.asLog
import eu.darken.androidstarter.common.debug.logging.log
import eu.darken.androidstarter.common.error.ErrorEventSource
import eu.darken.androidstarter.common.flow.setupCommonEventHandlers
import eu.darken.androidstarter.common.livedata.SingleLiveEvent
import eu.darken.androidstarter.common.navigation.NavEventSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn


abstract class ViewModel3(
    dispatcherProvider: DispatcherProvider,
) : ViewModel2(dispatcherProvider), NavEventSource, ErrorEventSource {

    override val navEvents = SingleLiveEvent<NavDirections?>()
    override val errorEvents = SingleLiveEvent<Throwable>()

    init {
        launchErrorHandler = CoroutineExceptionHandler { _, ex ->
            log(TAG) { "Error during launch: ${ex.asLog()}" }
            errorEvents.postValue(ex)
        }
    }

    override fun <T> Flow<T>.launchInViewModel() = this
        .setupCommonEventHandlers(TAG) { "launchInViewModel()" }
        .launchIn(vmScope)

}