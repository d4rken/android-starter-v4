package eu.darken.androidstarter.main.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.main.core.SomeRepo
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExampleReceiver : BroadcastReceiver() {

    @Inject lateinit var someRepo: SomeRepo

    override fun onReceive(context: Context, intent: Intent) {
        Timber.v("onReceive(%s, %s)", context, intent)
        if (intent.action != Intent.ACTION_HEADSET_PLUG) {
            Timber.w("Unknown action: %s", intent.action)
            return
        }
    }
}
