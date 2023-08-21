package eu.darken.androidstarter

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.getkeepsafe.relinker.ReLinker
import dagger.hilt.android.HiltAndroidApp
import eu.darken.androidstarter.common.BuildConfigWrap
import eu.darken.androidstarter.common.debug.autoreport.AutoReporting
import eu.darken.androidstarter.common.debug.logging.LogCatLogger
import eu.darken.androidstarter.common.debug.logging.Logging
import eu.darken.androidstarter.common.debug.logging.asLog
import eu.darken.androidstarter.common.debug.logging.log
import eu.darken.androidstarter.common.debug.logging.logTag
import eu.darken.androidstarter.common.theming.Theming
import javax.inject.Inject

@HiltAndroidApp
open class App : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var bugReporter: AutoReporting
    @Inject lateinit var theming: Theming

    override fun onCreate() {
        super.onCreate()
        if (BuildConfigWrap.DEBUG) {
            Logging.install(LogCatLogger())
            log(TAG) { "BuildConfig.DEBUG=true" }
        }

        ReLinker
            .log { message -> log(TAG) { "ReLinker: $message" } }
            .loadLibrary(this, "bugsnag-plugin-android-anr")

        bugReporter.setup()

        theming.setup()

        log(TAG) { "onCreate() done! ${Exception().asLog()}" }
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setMinimumLoggingLevel(android.util.Log.VERBOSE)
        .setWorkerFactory(workerFactory)
        .build()

    companion object {
        internal val TAG = logTag("App")
    }
}
