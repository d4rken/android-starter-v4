package eu.darken.androidstarter.settings.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.R
import eu.darken.androidstarter.databinding.SettingsActivityBinding
import eu.darken.androidstarter.settings.IndexFragment

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private val vm: SettingsActivityVM by viewModels()
    private lateinit var ui: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setSupportActionBar(ui.toolbar)

        if (savedInstanceState == null) {
            title = getString(R.string.general_settings_label)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, IndexFragment())
                .commit()
        } else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.general_settings_label)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean = if (supportFragmentManager.popBackStackImmediate()) {
        true
    } else {
        finish()
        super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment
        ).apply {
            arguments = args
            setTargetFragment(caller, 0)
        }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .addToBackStack(null)
            .commit()
        title = pref.title
        return true
    }

    companion object {
        const val TITLE_TAG = "settingsActivityTitle"
    }
}
