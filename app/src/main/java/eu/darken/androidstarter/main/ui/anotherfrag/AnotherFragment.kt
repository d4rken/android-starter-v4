package eu.darken.androidstarter.main.ui.anotherfrag

import android.os.Bundle
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.R
import eu.darken.androidstarter.common.ui2.Fragment2
import eu.darken.androidstarter.common.viewbinding.viewBinding
import eu.darken.androidstarter.databinding.AnotherFragmentBinding

//import eu.darken.kotlinstarter.common.vdc.vdcsAssisted

@AndroidEntryPoint
class AnotherFragment : Fragment2(R.layout.another_fragment) {

    private val vm: AnotherFragmentVM by viewModels()
    private val binding: AnotherFragmentBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}
