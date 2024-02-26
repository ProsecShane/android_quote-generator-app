package com.prosecshane.quoteapp.data.remote

/**
 * Singleton object that holds constants for API calls.
 */
object QuoteApiConstants {
    /**
     * Bearer token in encrypted form. Used in API calls.
     */
    const val EncryptedBearerToken = "9aSzl9jPlJUBlIXTru8Nr7oiklWvxlNwfhxMrkMpa4ww2r2F"

    /**
     * Two headers for API calls. They define the correct aspects for the call.
     */
    const val AcceptHeader = "accept: application/json"
    const val ContentTypeHeader = "content-type: application/json"

    /**
     * URL link to the API.
     */
    const val BaseURL = "https://api.fireworks.ai/inference/v1/chat/"
}
