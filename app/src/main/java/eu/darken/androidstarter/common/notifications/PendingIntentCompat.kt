package eu.darken.androidstarter.common.notifications

import android.app.PendingIntent
import eu.darken.androidstarter.common.hasApiLevel

object PendingIntentCompat {
    val FLAG_IMMUTABLE: Int = if (hasApiLevel(31)) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        0
    }
}