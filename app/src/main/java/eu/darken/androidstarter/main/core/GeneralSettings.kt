package eu.darken.androidstarter.main.core

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.androidstarter.common.debug.logging.logTag
import eu.darken.androidstarter.common.preferences.PreferenceStoreMapper
import eu.darken.androidstarter.common.preferences.Settings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneralSettings @Inject constructor(
    @ApplicationContext private val context: Context
) : Settings() {


    var isBugTrackingEnabled: Boolean
        get() = preferences.getBoolean(PK_BUGTRACKING_ENABLED, true)
        set(value) = preferences.edit().putBoolean(PK_BUGTRACKING_ENABLED, value).apply()

    var isPreviewEnabled: Boolean = true

    override val preferenceDataStore: PreferenceDataStore = object : PreferenceStoreMapper() {
        override fun getBoolean(key: String, defValue: Boolean): Boolean = when (key) {
            PK_BUGTRACKING_ENABLED -> isBugTrackingEnabled
            else -> super.getBoolean(key, defValue)
        }

        override fun putBoolean(key: String, value: Boolean) = when (key) {
            PK_BUGTRACKING_ENABLED -> isBugTrackingEnabled = value
            else -> super.putBoolean(key, value)
        }
    }

    override val preferences: SharedPreferences = context.getSharedPreferences("settings_core", Context.MODE_PRIVATE)

    companion object {
        internal val TAG = logTag("Core", "Settings")
        private const val PK_BUGTRACKING_ENABLED = "core.bugtracking.enabled"
    }
}