package eu.darken.androidstarter.main.ui.settings.support

import android.content.Intent
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.androidstarter.common.BuildConfigWrap
import eu.darken.androidstarter.common.EmailTool
import eu.darken.androidstarter.common.InstallId
import eu.darken.androidstarter.common.coroutine.DispatcherProvider
import eu.darken.androidstarter.common.livedata.SingleLiveEvent
import eu.darken.androidstarter.common.uix.ViewModel3
import javax.inject.Inject

@HiltViewModel
class SupportFragmentVM @Inject constructor(
    private val handle: SavedStateHandle,
    private val emailTool: EmailTool,
    private val installId: InstallId,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel3(dispatcherProvider) {

    val emailEvent = SingleLiveEvent<Intent>()
    val clipboardEvent = SingleLiveEvent<String>()

    fun sendSupportMail() = launch {

        val bodyInfo = StringBuilder("\n\n\n")

        bodyInfo.append("--- Infos for the developer ---\n")

        bodyInfo.append("App version: ").append(BuildConfigWrap.VERSION_DESCRIPTION).append("\n")

        bodyInfo.append("Device: ").append(Build.FINGERPRINT).append("\n")
        bodyInfo.append("Install ID: ").append(installId.id).append("\n")

        val email = EmailTool.Email(
            receipients = listOf("support@darken.eu"),
            subject = "[SD Maid] Question/Suggestion/Request\n",
            body = bodyInfo.toString()
        )

        emailEvent.postValue(emailTool.build(email))
    }

    fun copyInstallID() = launch {
        clipboardEvent.postValue(installId.id)
    }
}