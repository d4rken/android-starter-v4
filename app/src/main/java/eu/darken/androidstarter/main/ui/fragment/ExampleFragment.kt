package eu.darken.androidstarter.main.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.R
import eu.darken.androidstarter.common.observe2
import eu.darken.androidstarter.common.smart.SmartFragment
import eu.darken.androidstarter.common.viewbinding.viewBindingLazy
import eu.darken.androidstarter.databinding.ExampleFragmentBinding

@AndroidEntryPoint
class ExampleFragment : SmartFragment(R.layout.example_fragment) {

    private val vm: ExampleFragmentVM by viewModels()
    private val binding: ExampleFragmentBinding by viewBindingLazy()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_exampleFragment_to_anotherFragment)
            }

            vm.state.observe2(this@ExampleFragment) { emojiText.text = it.data }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_example, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_help -> {
            Snackbar.make(requireView(), R.string.app_name, Snackbar.LENGTH_SHORT).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
