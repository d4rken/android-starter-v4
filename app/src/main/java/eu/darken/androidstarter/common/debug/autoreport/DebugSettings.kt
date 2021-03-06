package eu.darken.androidstarter.common.debug.autoreport

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.androidstarter.common.preferences.createFlowPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugSettings @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val prefs by lazy {
        context.getSharedPreferences("debug_settings", Context.MODE_PRIVATE)
    }

    val isAutoReportingEnabled = prefs.createFlowPreference("debug.bugreport.automatic.enabled", true)

}