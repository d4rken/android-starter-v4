package eu.darken.androidstarter.main.ui.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.androidstarter.common.coroutine.DispatcherProvider
import eu.darken.androidstarter.common.debug.logging.log
import eu.darken.androidstarter.common.navigation.navArgs
import eu.darken.androidstarter.common.uix.ViewModel3
import eu.darken.androidstarter.main.core.SomeRepo
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MainFragmentVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    someRepo: SomeRepo,
) : ViewModel3(dispatcherProvider = dispatcherProvider) {
    private val navArgs by handle.navArgs<MainFragmentArgs>()

    val listItems = combine(
        someRepo.countsWhileSubscribed,
        someRepo.countsAlways,
        someRepo.emojis
    ) { whileSubbed, always, emoji ->
        listOf(
            SomeAdapter.Item("whileSubbed1", number = whileSubbed) {},
            SomeAdapter.Item("always2", number = always) {},
            SomeAdapter.Item("emoji $emoji", number = emoji.hashCode().toLong()) {},
        )
    }.asLiveData2()

    init {
        log { "ViewModel: $this" }
        log { "SavedStateHandle: ${handle.keys()}" }
        log { "Persisted value: ${handle.get<Long>("lastValue")}" }
        log { "Default args: ${handle.get<String>("fragmentArg")}" }
//        Timber.d("NavArgs: %s", navArgs)
    }
}