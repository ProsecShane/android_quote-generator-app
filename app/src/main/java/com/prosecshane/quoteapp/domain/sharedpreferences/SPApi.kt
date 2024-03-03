package com.prosecshane.quoteapp.domain.sharedpreferences

interface SPApi {
    suspend fun <T> get(key: String, fallback: T): T
    suspend fun set(key: String, value: Any)
}
