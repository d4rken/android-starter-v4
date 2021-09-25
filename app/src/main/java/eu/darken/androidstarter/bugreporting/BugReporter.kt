package eu.darken.androidstarter.bugreporting

import android.content.Context
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.androidstarter.App
import eu.darken.androidstarter.common.InstallId
import eu.darken.androidstarter.common.debug.bugsnag.BugsnagErrorHandler
import eu.darken.androidstarter.common.debug.bugsnag.BugsnagLogger
import eu.darken.androidstarter.common.debug.bugsnag.NOPBugsnagErrorHandler
import eu.darken.androidstarter.common.debug.logging.Logging
import eu.darken.androidstarter.common.debug.logging.log
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class BugReporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bugReporterSettings: BugReporterSettings,
    private val installId: InstallId,
    private val bugsnagLogger: Provider<BugsnagLogger>,
    private val bugsnagErrorHandler: Provider<BugsnagErrorHandler>,
    private val nopBugsnagErrorHandler: Provider<NOPBugsnagErrorHandler>,
) {

    fun setup() {
        val isEnabled = bugReporterSettings.isEnabled.value
        log(TAG) { "setup(): isEnabled=$isEnabled" }

        try {
            val bugsnagConfig = Configuration.load(context).apply {
                if (bugReporterSettings.isEnabled.value) {
                    Logging.install(bugsnagLogger.get())
                    setUser(installId.id, null, null)
                    autoTrackSessions = true
                    addOnError(bugsnagErrorHandler.get())
                    log(TAG) { "Bugsnag setup done!" }
                } else {
                    autoTrackSessions = false
                    addOnError(nopBugsnagErrorHandler.get())
                    log(TAG) { "Installing Bugsnag NOP error handler due to user opt-out!" }
                }
            }

            Bugsnag.start(context, bugsnagConfig)
        } catch (e: IllegalStateException) {
            log(TAG) { "Bugsnag API Key not configured." }
        }
    }

    companion object {
        private val TAG = App.logTag("BugReporter")
    }
}