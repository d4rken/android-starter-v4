package eu.darken.androidstarter.settings.general

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.androidstarter.common.coroutine.DispatcherProvider
import eu.darken.androidstarter.common.debug.logging.logTag
import eu.darken.androidstarter.common.uix.ViewModel3
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsFragmentVM @Inject constructor(
    private val handle: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel3(dispatcherProvider) {

    data class State(
        val isRecording: Boolean = false,
        val recordingPath: String = ""
    )

    companion object {
        private val TAG = logTag("Settings", "General", "VM")
    }
}