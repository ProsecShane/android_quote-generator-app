package com.prosecshane.quoteapp.data.sharedpreferences

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.prosecshane.quoteapp.domain.sharedpreferences.SPApi
import javax.inject.Inject

/**
 * Implementation of the SPApi interface.
 * A kind of API that works with SharedPreferences.
 *
 * @param appContext Context to create an instance of the SharedPreferences worker.
 */
class SPApiImpl @Inject constructor(
    appContext: Application
) : SPApi {
    /**
     * SharedPreferences worker to set and get values.
     */
    private val sp = appContext.getSharedPreferences(SPConstants.filename, Context.MODE_PRIVATE)

    /**
     * Function that gets a value of type and provides a fallback value
     * in case there is no such value.
     *
     * @param key The key that allows to get the value from the SharedPreferences file.
     * @param fallback The value returned if no value is found.
     * @return The fallback of the value stored in SharedPreferences of type T.
     */
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

    /**
     * Function that sets a value of type T with a certain key.
     *
     * @param key The key for the SharedPreferences file to store the value.
     * @param value Value to store.
     */
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
