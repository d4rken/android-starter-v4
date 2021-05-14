package eu.darken.androidstarter.common.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import eu.darken.androidstarter.App
import timber.log.Timber

abstract class VM : ViewModel() {
    val TAG: String = App.logTag("VM", javaClass.simpleName)

    init {
        Timber.tag(TAG).v("Initialized")
    }

    @CallSuper
    override fun onCleared() {
        Timber.tag(TAG).v("onCleared()")
        super.onCleared()
    }
}