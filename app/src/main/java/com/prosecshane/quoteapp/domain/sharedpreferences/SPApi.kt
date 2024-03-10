package com.prosecshane.quoteapp.domain.sharedpreferences

/**
 * Interface of kind of API that works with SharedPreferences.
 */
interface SPApi {
    /**
     * Function that needs to implement getting a value of type T
     * and providing a fallback value in case there is no such value.
     *
     * @param key The key that allows to get the value from the SharedPreferences file.
     * @param fallback The value returned if no value is found.
     * @return The fallback of the value stored in SharedPreferences of type T.
     */
    suspend fun <T> get(key: String, fallback: T): T

    /**
     * Function that needs to implement setting a value of type T
     * with a certain key.
     *
     * @param key The key for the SharedPreferences file to store the value.
     * @param value Value to store.
     */
    suspend fun set(key: String, value: Any)
}
