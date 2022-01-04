package eu.darken.androidstarter.common.preferences

import android.content.SharedPreferences
import com.squareup.moshi.Moshi

inline fun <reified T> moshiReader(
    moshi: Moshi,
    defaultValue: T,
): SharedPreferences.(key: String) -> T {
    val adapter = moshi.adapter(T::class.java)
    return { key ->
        getString(key, null)?.let { adapter.fromJson(it) } ?: defaultValue
    }
}

inline fun <reified T> moshiWriter(
    moshi: Moshi,
): SharedPreferences.Editor.(key: String, value: T) -> Unit {
    val adapter = moshi.adapter(T::class.java)
    return { key, value ->
        putString(key, value?.let { adapter.toJson(it) })
    }
}