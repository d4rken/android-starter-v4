package eu.darken.androidstarter

import android.app.Application
import com.getkeepsafe.relinker.ReLinker
import dagger.hilt.android.HiltAndroidApp
import eu.darken.androidstarter.common.debug.autoreport.AutoReporting
import eu.darken.androidstarter.common.debug.logging.*
import javax.inject.Inject

@HiltAndroidApp
open class App : Application() {

    @Inject lateinit var bugReporter: AutoReporting

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Logging.install(LogCatLogger())

        ReLinker
            .log { message -> log(TAG) { "ReLinker: $message" } }
            .loadLibrary(this, "bugsnag-plugin-android-anr")

        bugReporter.setup()

        log(TAG) { "onCreate() done! ${Exception().asLog()}" }
    }

    companion object {
        internal val TAG = logTag("AKSv4")
    }
}
