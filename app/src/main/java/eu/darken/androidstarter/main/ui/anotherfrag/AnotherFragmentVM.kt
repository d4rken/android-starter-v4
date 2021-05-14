package eu.darken.androidstarter.main.ui.anotherfrag

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.androidstarter.common.viewmodel.SmartVM
import eu.darken.androidstarter.main.core.SomeRepo
import javax.inject.Inject

@HiltViewModel
class AnotherFragmentVM @Inject constructor(
    private val handle: SavedStateHandle,
    private val someRepo: SomeRepo
) : SmartVM() {


//    @AssistedFactory
//    interface Factory : VDCFactory<AnotherFragmentVDC> {
//        fun create(handle: SavedStateHandle): AnotherFragmentVDC
//    }
}