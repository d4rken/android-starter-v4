package eu.darken.androidstarter.common.error

import eu.darken.androidstarter.common.livedata.SingleLiveEvent

interface ErrorEventSource {
    val errorEvents: SingleLiveEvent<Throwable>
}