package eu.darken.androidstarter.settings.ui

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.androidstarter.common.uix.ViewModel2
import javax.inject.Inject

@HiltViewModel
class SettingsActivityVM @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel2()