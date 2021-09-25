package eu.darken.androidstarter.common.smart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import eu.darken.androidstarter.App
import eu.darken.androidstarter.common.debug.logging.log


abstract class SmartFragment(
    @LayoutRes layoutRes: Int
) : Fragment(layoutRes) {

    @Suppress("JoinDeclarationAndAssignment")
    internal val tag: String

    init {
        tag = App.logTag("Fragment", this.javaClass.simpleName + "(" + Integer.toHexString(hashCode()) + ")")
    }

    override fun onAttach(context: Context) {
        log(tag) { "onAttach(context=$context)" }
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        log(tag) { "onCreate(savedInstanceState=$savedInstanceState)" }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        log(tag) { "onViewCreated(view=$view, savedInstanceState=$savedInstanceState)" }
        super.onViewCreated(view, savedInstanceState)
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        log(tag) { "onActivityCreated(savedInstanceState=$savedInstanceState)" }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        log(tag) { "onResume()" }
        super.onResume()
    }

    override fun onPause() {
        log(tag) { "onPause()" }
        super.onPause()
    }

    override fun onDestroyView() {
        log(tag) { "onDestroyView()" }
        super.onDestroyView()
    }

    override fun onDetach() {
        log(tag) { "onDetach()" }
        super.onDetach()
    }

    override fun onDestroy() {
        log(tag) { "onDestroy()" }
        super.onDestroy()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        log(tag) { "onActivityResult(requestCode=$requestCode, resultCode=$resultCode, data=$data)" }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
