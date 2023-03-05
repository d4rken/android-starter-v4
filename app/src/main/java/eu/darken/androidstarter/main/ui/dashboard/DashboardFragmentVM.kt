package eu.darken.androidstarter.main.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.androidstarter.common.BuildConfigWrap
import eu.darken.androidstarter.common.coroutine.DispatcherProvider
import eu.darken.androidstarter.common.debug.logging.Logging.Priority.ERROR
import eu.darken.androidstarter.common.debug.logging.asLog
import eu.darken.androidstarter.common.debug.logging.log
import eu.darken.androidstarter.common.github.GithubReleaseCheck
import eu.darken.androidstarter.common.navigation.navArgs
import eu.darken.androidstarter.common.uix.ViewModel3
import eu.darken.androidstarter.main.core.SomeRepo
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import net.swiftzer.semver.SemVer
import javax.inject.Inject

@HiltViewModel
class DashboardFragmentVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    someRepo: SomeRepo,
    githubReleaseCheck: GithubReleaseCheck,
) : ViewModel3(dispatcherProvider = dispatcherProvider) {
    private val navArgs by handle.navArgs<DashboardFragmentArgs>()

    val newRelease = flow {
        val latestRelease = try {
            githubReleaseCheck.latestRelease("d4rken", "android-starter-v4")
        } catch (e: Exception) {
            log(TAG, ERROR) { "Release check failed: ${e.asLog()}" }
            null
        }
        emit(latestRelease)
    }
        .filterNotNull()
        .filter {
            val current = try {
                SemVer.parse(BuildConfigWrap.VERSION_NAME.removePrefix("v"))
            } catch (e: IllegalArgumentException) {
                log(TAG, ERROR) { "Failed to parse current version: ${e.asLog()}" }
                return@filter false
            }
            log(TAG) { "Current version is $current" }

            val latest = try {
                SemVer.parse(it.tagName.removePrefix("v")).nextMinor()
            } catch (e: IllegalArgumentException) {
                log(TAG, ERROR) { "Failed to parse current version: ${e.asLog()}" }
                return@filter false
            }
            log(TAG) { "Latest version is $latest" }
            current < latest
        }
        .asLiveData2()

    val listItems = combine(
        someRepo.countsWhileSubscribed,
        someRepo.countsAlways,
        someRepo.emojis
    ) { whileSubbed, always, emoji ->
        listOf(
            SomeAdapter.Item("whileSubbed1", number = whileSubbed) {},
            SomeAdapter.Item("always2", number = always) {},
            SomeAdapter.Item("emoji3 $emoji", number = emoji.hashCode().toLong()) {},
        )
    }.asLiveData2()

    init {
        log { "ViewModel: $this" }
        log { "SavedStateHandle: ${handle.keys()}" }
        log { "Persisted value: ${handle.get<Long>("lastValue")}" }
        log { "Default args: ${handle.get<String>("fragmentArg")}" }
//        Timber.d("NavArgs: %s", navArgs)
    }
}