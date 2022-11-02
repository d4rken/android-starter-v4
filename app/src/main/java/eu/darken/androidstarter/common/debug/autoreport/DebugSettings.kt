package eu.darken.androidstarter.common.debug.autoreport

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.androidstarter.common.datastore.PreferenceScreenData
import eu.darken.androidstarter.common.datastore.PreferenceStoreMapper
import eu.darken.androidstarter.common.datastore.createValue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugSettings @Inject constructor(
    @ApplicationContext private val context: Context,
) : PreferenceScreenData {

    private val Context.dataStore by preferencesDataStore(name = "debug_settings")

    override val dataStore: DataStore<Preferences>
        get() = context.dataStore

    val isAutoReportingEnabled = dataStore.createValue("debug.bugreport.automatic.enabled", true)

    override val mapper = PreferenceStoreMapper(
        isAutoReportingEnabled,
    )

}
