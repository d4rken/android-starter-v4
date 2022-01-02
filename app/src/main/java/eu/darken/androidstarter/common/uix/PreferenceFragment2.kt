package eu.darken.androidstarter.common.uix

import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.XmlRes
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import eu.darken.androidstarter.common.preferences.Settings

abstract class PreferenceFragment2
    : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    abstract val settings: Settings

    @get:XmlRes
    abstract val preferenceFile: Int

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = settings.preferenceDataStore
        settings.preferences.registerOnSharedPreferenceChangeListener(this)
        refreshPreferenceScreen()
    }

    override fun onDestroy() {
        settings.preferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    override fun getCallbackFragment(): Fragment? = parentFragment

    fun refreshPreferenceScreen() {
        if (preferenceScreen != null) preferenceScreen = null
        addPreferencesFromResource(preferenceFile)
        onPreferencesCreated()
    }

    open fun onPreferencesCreated() {

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

    }
}