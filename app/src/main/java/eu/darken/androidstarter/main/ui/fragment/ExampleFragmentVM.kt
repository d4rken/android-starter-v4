package eu.darken.androidstarter.main.ui.fragment

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.androidstarter.common.coroutine.DispatcherProvider
import eu.darken.androidstarter.common.navigation.navArgs
import eu.darken.androidstarter.common.viewmodel.SmartVM
import eu.darken.androidstarter.main.core.SomeRepo
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExampleFragmentVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    someRepo: SomeRepo,
) : SmartVM(dispatcherProvider = dispatcherProvider) {
    private val navArgs by handle.navArgs<ExampleFragmentArgs>()

    private val stateFlow = combine(
        someRepo.countsWhileSubscribed,
        someRepo.countsAlways,
        someRepo.emojis
    ) { whileSubbed, always, emoji ->
        State(
            data = "WhileSubbed=$whileSubbed Always=$always $emoji"
        )
    }
    val state = stateFlow.asLiveData2()

    init {
        Timber.d("ViewModel: %s", this)
        Timber.d("SavedStateHandle: %s", handle.keys())
        Timber.d("Persisted value: %s", handle.get<Long>("lastValue"))
        Timber.d("Default args: %s", handle.get<String>("fragmentArg"))
//        Timber.d("NavArgs: %s", navArgs)
    }

    data class State(
        val data: String = "?"
    )
}