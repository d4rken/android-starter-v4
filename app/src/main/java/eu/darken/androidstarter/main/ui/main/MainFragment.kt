package eu.darken.androidstarter.main.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.R
import eu.darken.androidstarter.common.lists.differ.update
import eu.darken.androidstarter.common.lists.setupDefaults
import eu.darken.androidstarter.common.navigation.doNavigate
import eu.darken.androidstarter.common.observe2
import eu.darken.androidstarter.common.uix.Fragment3
import eu.darken.androidstarter.common.viewbinding.viewBinding
import eu.darken.androidstarter.databinding.MainFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment3(R.layout.main_fragment) {

    override val vm: MainFragmentVM by viewModels()
    override val ui: MainFragmentBinding by viewBinding()

    @Inject lateinit var someAdapter: SomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.toolbar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_help -> {
                        Snackbar.make(requireView(), R.string.app_name, Snackbar.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_settings -> {
                        doNavigate(MainFragmentDirections.actionExampleFragmentToSettingsContainerFragment())
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }

        ui.list.setupDefaults(someAdapter)

        vm.listItems.observe2(this@MainFragment, ui) {
            someAdapter.update(it)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}
