package com.prosecshane.quoteapp.domain.repository

import com.prosecshane.quoteapp.domain.network.GenerationStatus

/**
 * An interface that will produce instances of objects that will work with API.
 */
interface QuoteClient {
    /**
     * A suspend function that through API requests for the quote to be generated.
     * Needs to be implemented.
     *
     * @param keywords The keywords by which the quote has to be generated.
     * @param callback The callback that will process the result of the API call,
     * represented by an instance of [GenerationStatus].
     */
    suspend fun generateQuote(
        keywords: String,
        callback: (GenerationStatus) -> Unit,
    )
}
