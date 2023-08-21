package eu.darken.androidstarter.main.core

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.androidstarter.common.datastore.PreferenceScreenData
import eu.darken.androidstarter.common.datastore.PreferenceStoreMapper
import eu.darken.androidstarter.common.datastore.createValue
import eu.darken.androidstarter.common.debug.logging.logTag
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneralSettings @Inject constructor(
    @ApplicationContext private val context: Context,
) : PreferenceScreenData {

    private val Context.dataStore by preferencesDataStore(name = "settings_core")

    override val dataStore: DataStore<Preferences>
        get() = context.dataStore

    val deviceLabel = dataStore.createValue("core.device.label", Build.DEVICE)

    val isAutoReportingEnabled = dataStore.createValue("debug.bugreport.automatic.enabled", true)

    override val mapper = PreferenceStoreMapper(
        isAutoReportingEnabled,
        deviceLabel
    )

    companion object {
        internal val TAG = logTag("Core", "Settings")
    }
}