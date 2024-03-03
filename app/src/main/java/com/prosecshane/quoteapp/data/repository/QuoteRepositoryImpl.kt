package com.prosecshane.quoteapp.data.repository

import com.prosecshane.quoteapp.data.local.QuoteDao
import com.prosecshane.quoteapp.domain.model.Quote
import com.prosecshane.quoteapp.domain.network.GenerationStatus
import com.prosecshane.quoteapp.domain.repository.QuoteClient
import com.prosecshane.quoteapp.domain.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Class implementation of the [QuoteRepository] interface.
 * Manages data with [flow]s.
 *
 * @param client A [QuoteClient] variable to receive processed results of an API calls from.
 * @param dao A [QuoteDao] variable to write and read the database.
 */
class QuoteRepositoryImpl @Inject constructor(
    private val client: QuoteClient,
    private val dao: QuoteDao,
): QuoteRepository {
    /**
     * Creates a [flow] that is emitting different [GenerationStatus] variables
     * to be collected by a ViewModel. Flows on IO thread.
     *
     * @param keywords Keywords variable that is passed on to the [QuoteClient].
     * @return Flow that emits results as [GenerationStatus] variables.
     */
    override suspend fun generateQuote(keywords: String): Flow<GenerationStatus> = flow {
        emit(GenerationStatus.InProgress)
        try {
            var result: GenerationStatus? = null
            client.generateQuote(keywords) { result = it }
            emit(result ?: GenerationStatus.Error("Null generation status"))
         } catch (e: Exception) {
             emit(GenerationStatus.Error("Quote fetch failed"))
         }
    }.flowOn(Dispatchers.IO)

    /**
     * A suspend function that by working with a Dao instance gets
     * all locally saved [Quote]s.
     *
     * @return List of all locally saved quotes.
     */
    override suspend fun getLocalQuotes(): List<Quote> {
        return dao.getQuotes()
    }

    /**
     * A suspend function that by working with a Dao instance saves
     * a given [Quote] to the local database.
     *
     * @param quote A [Quote] to save.
     */
    override suspend fun saveQuote(quote: Quote) {
        dao.addQuote(quote)
    }

    /**
     * A suspend function that by working with a Dao instance finds
     * by its ID and deletes a certain [Quote] from the local database.
     *
     * @param id ID of the quote that needs to be deleted from the database.
     */
    override suspend fun deleteLocalQuote(id: String) {
        dao.removeQuote(id)
    }

    /**
     * A suspend function that by working with a Dao instance clears
     * the table with the saved locally quotes.
     */
    override suspend fun clearLocalQuotes() {
        dao.clearQuotes()
    }
}
