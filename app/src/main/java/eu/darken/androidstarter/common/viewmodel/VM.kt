package eu.darken.androidstarter.common.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import eu.darken.androidstarter.App
import eu.darken.androidstarter.common.debug.logging.log

abstract class VM : ViewModel() {
    val TAG: String = App.logTag("VM", javaClass.simpleName)

    init {
        log(TAG) { "Initialized" }
    }

    @CallSuper
    override fun onCleared() {
        log(TAG) { "onCleared()" }
        super.onCleared()
    }
}