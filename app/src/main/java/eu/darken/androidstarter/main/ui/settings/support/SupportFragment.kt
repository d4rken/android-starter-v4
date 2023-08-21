package eu.darken.androidstarter.main.ui.settings.support

import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.androidstarter.R
import eu.darken.androidstarter.common.ClipboardHelper
import eu.darken.androidstarter.common.WebpageTool
import eu.darken.androidstarter.common.debug.recorder.ui.RecorderConsentDialog
import eu.darken.androidstarter.common.observe2
import eu.darken.androidstarter.common.uix.PreferenceFragment2
import eu.darken.androidstarter.main.core.GeneralSettings
import javax.inject.Inject

@Keep
@AndroidEntryPoint
class SupportFragment : PreferenceFragment2() {

    private val vm: SupportViewModel by viewModels()

    override val preferenceFile: Int = R.xml.preferences_support
    @Inject lateinit var generalSettings: GeneralSettings

    override val settings: GeneralSettings by lazy { generalSettings }

    @Inject lateinit var clipboardHelper: ClipboardHelper
    @Inject lateinit var webpageTool: WebpageTool

    private val installIdPref by lazy { findPreference<Preference>("support.installid")!! }
    private val debugLogPref by lazy { findPreference<Preference>("support.debuglog")!! }

    override fun onPreferencesCreated() {
        installIdPref.setOnPreferenceClickListener {
            vm.copyInstallID()
            true
        }
        super.onPreferencesCreated()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.clipboardEvent.observe2(this) { installId ->
            Snackbar.make(requireView(), installId, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.general_copy_action) {
                    clipboardHelper.copyToClipboard(installId)
                }
                .show()
        }

        vm.isRecording.observe2(this) { isRecording ->
            debugLogPref.setIcon(
                if (isRecording) R.drawable.ic_cancel_24
                else R.drawable.ic_bug_report_24
            )
            debugLogPref.setTitle(
                if (isRecording) R.string.debug_debuglog_stop_action
                else R.string.debug_debuglog_record_action
            )
            debugLogPref.setOnPreferenceClickListener {
                if (isRecording) {
                    vm.stopDebugLog()
                } else {
                    RecorderConsentDialog(requireContext(), webpageTool).showDialog {
                        vm.startDebugLog()
                    }
                }
                true
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}