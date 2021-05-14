package eu.darken.androidstarter.main.ui

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.androidstarter.common.coroutine.DispatcherProvider
import eu.darken.androidstarter.common.viewmodel.SmartVM
import eu.darken.androidstarter.main.core.SomeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainActivityVM @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    handle: SavedStateHandle,
    private val someRepo: SomeRepo
) : SmartVM(dispatcherProvider = dispatcherProvider) {

    private val stateFlow = MutableStateFlow(State())
    val state = stateFlow
        .onEach { Timber.v("New state: %s", it) }
        .asLiveData2()

    init {
        Timber.d("ViewModel: %s", this)
        Timber.d("SavedStateHandle: %s", handle.keys())
        Timber.d("Persisted value: %s", handle.get<String>("key"))
        handle.set("key", "valueActivity")
    }

    fun onGo() {
        stateFlow.value = stateFlow.value.copy(ready = true)
    }

    data class State(
        val ready: Boolean = false
    )

}