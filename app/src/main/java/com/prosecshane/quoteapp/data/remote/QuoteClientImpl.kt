package com.prosecshane.quoteapp.data.remote

import com.prosecshane.quoteapp.domain.model.util.RequestConstants
import com.prosecshane.quoteapp.domain.model.util.defaultRequestBody
import com.prosecshane.quoteapp.domain.network.GenerationStatus
import com.prosecshane.quoteapp.domain.repository.QuoteClient
import javax.inject.Inject

/**
 * This is an implementation class of the [QuoteClient] interface.
 * Allows to interact with the [QuoteApi] and process different outcomes of the call.
 *
 * @param api Instance of [QuoteApi] to make API calls and get responses from it.
 */
class QuoteClientImpl @Inject constructor(
    private val api: QuoteApi
): QuoteClient {
    /**
     * Using the [api] variable, generates a quote based on given information.
     *
     * @param keywords Given keywords to put in the body of the API call.
     * Used to create a body with [defaultRequestBody] function.
     * @param callback Callback to use on the resulting [GenerationStatus] variable.
     */
    override suspend fun generateQuote(
        keywords: String,
        callback: (GenerationStatus) -> Unit,
    ) {
        if (keywords == "") {
            callback(GenerationStatus.Error("The keywords fields is empty!"))
            return
        }
        try {
            val response = api.generateQuote(defaultRequestBody(keywords))
            if (response.isSuccessful) {
                val result = response.body()
                if (result != null) {
                    val quote = result.choices.first().message.content
                    if (quote.contains(RequestConstants.errorMessage)) {
                        callback(GenerationStatus.Error("AI model could not generate a response! Try again or change the topic of your prompt."))
                    } else {
                        callback(GenerationStatus.Success(quote))
                    }
                } else {
                    callback(GenerationStatus.Error("Empty response."))
                }
            } else {
                callback(GenerationStatus.Error("Bad response (${response.code()})."))
            }
        } catch (e: Exception) {
            callback(GenerationStatus.Error("Call failed (${e.message})."))
        }
    }
}
