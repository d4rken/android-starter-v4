package eu.darken.androidstarter.settings.general

import androidx.annotation.Keep
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.R
import eu.darken.androidstarter.common.uix.PreferenceFragment2
import eu.darken.androidstarter.main.core.GeneralSettings
import javax.inject.Inject

@Keep
@AndroidEntryPoint
class GeneralSettingsFragment : PreferenceFragment2() {

    private val vdc: GeneralSettingsFragmentVM by viewModels()

    @Inject lateinit var debugSettings: GeneralSettings

    override val settings: GeneralSettings by lazy { debugSettings }
    override val preferenceFile: Int = R.xml.preferences_general

//    private val recordPref by lazy { findPreference<Preference>(GeneralSettings.PKEY_RECORD_DEBUG)!! }

}