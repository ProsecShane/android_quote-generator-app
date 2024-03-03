package com.prosecshane.quoteapp.data.sharedpreferences

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.prosecshane.quoteapp.domain.sharedpreferences.SPApi
import javax.inject.Inject

class SPApiImpl @Inject constructor(
    appContext: Application
) : SPApi {
    private val sp = appContext.getSharedPreferences(SPConstants.filename, Context.MODE_PRIVATE)

    override suspend fun <T> get(key: String, fallback: T): T {
        val result = when (fallback) {
            is Int -> sp.getInt(key, fallback)
            is Long -> sp.getLong(key, fallback)
            is Float -> sp.getFloat(key, fallback)
            is String -> sp.getString(key, fallback)
            is Boolean -> sp.getBoolean(key, fallback)
            else ->
                throw Exception("Type ${fallback!!::class.java.simpleName} not supported for SPApi")
        }
        return result as T
    }

    override suspend fun set(key: String, value: Any) {
        sp.edit(commit = true) {
            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                else ->
                    throw Exception("Type ${value::class.java.simpleName} not supported for SPApi")
            }
        }
    }
}
