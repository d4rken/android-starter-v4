package eu.darken.androidstarter.common.smart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.darken.androidstarter.App
import eu.darken.androidstarter.common.debug.logging.log

abstract class SmartActivity : AppCompatActivity() {
    internal val tag: String =
        App.logTag("Activity", this.javaClass.simpleName + "(" + Integer.toHexString(hashCode()) + ")")

    override fun onCreate(savedInstanceState: Bundle?) {
        log(tag) { "onCreate(savedInstanceState=$savedInstanceState)" }
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        log(tag) { "onResume()" }
        super.onResume()
    }

    override fun onPause() {
        log(tag) { "onPause()" }
        super.onPause()
    }

    override fun onDestroy() {
        log(tag) { "onDestroy()" }
        super.onDestroy()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        log(tag) { "onActivityResult(requestCode=$requestCode, resultCode=$resultCode, data=data)" }
        super.onActivityResult(requestCode, resultCode, data)
    }

}