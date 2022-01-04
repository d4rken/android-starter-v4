package eu.darken.androidstarter.main.core

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.androidstarter.common.debug.logging.logTag
import eu.darken.androidstarter.common.preferences.PreferenceStoreMapper
import eu.darken.androidstarter.common.preferences.Settings
import eu.darken.androidstarter.common.preferences.createFlowPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneralSettings @Inject constructor(
    @ApplicationContext private val context: Context
) : Settings() {

    override val preferences: SharedPreferences = context.getSharedPreferences("settings_core", Context.MODE_PRIVATE)

    val isBugTrackingEnabled = preferences.createFlowPreference("core.bugtracking.enabled", true)

    override val preferenceDataStore: PreferenceDataStore = PreferenceStoreMapper(
        isBugTrackingEnabled
    )

    companion object {
        internal val TAG = logTag("Core", "Settings")
    }
}