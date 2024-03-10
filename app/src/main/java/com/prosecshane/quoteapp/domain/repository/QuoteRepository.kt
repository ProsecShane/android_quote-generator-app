package com.prosecshane.quoteapp.domain.repository

import com.prosecshane.quoteapp.domain.model.Quote
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

    /**
     * A suspend function that by working with a Dao instance gets
     * all locally saved [Quote]s. Needs to be implemented.
     *
     * @return List of all locally saved quotes.
     */
    suspend fun getLocalQuotes(): List<Quote>

    /**
     * A suspend function that by working with a Dao instance saves
     * a given [Quote] to the local database. Needs to be implemented.
     *
     * @param quote A [Quote] to save.
     */
    suspend fun saveQuote(quote: Quote)

    /**
     * A suspend function that by working with a Dao instance finds
     * by its ID and deletes a certain [Quote] from the local database.
     * Needs to be implemented.
     *
     * @param id ID of the quote that needs to be deleted from the database.
     */
    suspend fun deleteLocalQuote(id: String)

    /**
     * A suspend function that by working with a Dao instance clears
     * the table with the saved locally quotes. Needs to be implemented.
     */
    suspend fun clearLocalQuotes()
}
