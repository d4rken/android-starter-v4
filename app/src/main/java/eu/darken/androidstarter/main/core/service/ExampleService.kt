package eu.darken.androidstarter.main.core.service

import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.common.ui2.Service2
import javax.inject.Inject


@AndroidEntryPoint
class ExampleService : Service2() {

    @Inject lateinit var binder: ExampleBinder

    override fun onBind(intent: Intent): IBinder = binder

}