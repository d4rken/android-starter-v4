package eu.darken.androidstarter.settings

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.R
import eu.darken.androidstarter.common.uix.Fragment2
import eu.darken.androidstarter.common.viewbinding.viewBinding
import eu.darken.androidstarter.databinding.SettingsFragmentBinding
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class SettingsFragment : Fragment2(R.layout.settings_fragment),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private val vm: SettingsFragmentVM by viewModels()
    private val ui: SettingsFragmentBinding by viewBinding()
    private val screenInfos = ArrayList<ScreenInfo>()

    @Parcelize
    data class ScreenInfo(
        val fragmentClass: String,
        val screenTitle: String?
    ) : Parcelable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.addOnBackStackChangedListener {
            val backStackCnt = childFragmentManager.backStackEntryCount
            val newScreenInfo = when {
                backStackCnt < screenInfos.size -> {
                    // We popped the backstack, restore the underlying screen infos
                    // If there are none left, we are at the index again
                    screenInfos.removeLastOrNull()
                    screenInfos.lastOrNull() ?: ScreenInfo(
                        fragmentClass = SettingsIndexFragment::class.qualifiedName!!,
                        screenTitle = getString(R.string.label_settings)
                    )
                }
                else -> {
                    // We added the current fragment to the stack, the new fragment's infos were already set, do nothing.
                    null
                }
            }

            newScreenInfo?.let { setCurrentScreenInfo(it) }
        }

        if (savedInstanceState == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, SettingsIndexFragment())
                .commit()
        } else {
            savedInstanceState.getParcelableArrayList<ScreenInfo>(BKEY_SCREEN_INFOS)?.let {
                screenInfos.addAll(it)
            }
            screenInfos.lastOrNull()?.let { setCurrentScreenInfo(it) }
        }

        ui.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(BKEY_SCREEN_INFOS, screenInfos)
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        val screenInfo = ScreenInfo(
            fragmentClass = pref.fragment,
            screenTitle = pref.title?.toString()
        )

        val args = Bundle().apply {
            putAll(pref.extras)
            putString(BKEY_SCREEN_TITLE, screenInfo.screenTitle)
        }

        val fragment = childFragmentManager.fragmentFactory
            .instantiate(this::class.java.classLoader!!, pref.fragment)
            .apply {
                arguments = args
                setTargetFragment(caller, 0)
            }

        setCurrentScreenInfo(screenInfo)
        screenInfos.add(screenInfo)

        childFragmentManager.beginTransaction().apply {
            replace(R.id.content_frame, fragment)
            addToBackStack(null)
        }.commit()

        return true
    }


    private fun setCurrentScreenInfo(info: ScreenInfo) {
        ui.toolbar.title = info.screenTitle
    }

    companion object {
        private const val BKEY_SCREEN_TITLE = "preferenceScreenTitle"
        private const val BKEY_SCREEN_INFOS = "preferenceScreenInfos"
    }
}
