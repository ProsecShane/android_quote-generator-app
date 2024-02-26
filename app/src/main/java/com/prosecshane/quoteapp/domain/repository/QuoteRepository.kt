package com.prosecshane.quoteapp.domain.repository

import com.prosecshane.quoteapp.domain.network.GenerationStatus
import kotlinx.coroutines.flow.Flow

/**
 * An interface that will produce instances of objects
 * that will work with a client through flow.
 */
interface QuoteRepository {
    /**
     * A suspend function that by working with a client instance gets
     * different instances of [GenerationStatus] and emits them through flow.
     * Needs to be implemented.
     *
     * @param keywords The keywords that by which the quote has to be generated.
     */
    suspend fun generateQuote(keywords: String): Flow<GenerationStatus>
}
