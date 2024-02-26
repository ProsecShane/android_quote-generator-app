package com.prosecshane.quoteapp.data.repository

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
 * @param client A [QuoteClient] variable to receive processed results of an API calls.
 */
class QuoteRepositoryImpl @Inject constructor(
    private val client: QuoteClient
): QuoteRepository {
    /**
     * Creates a [flow] that is emitting different variables
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
}
