package com.prosecshane.quoteapp.domain.model

/**
 * A data class that represents one variant of the response
 * from AI model's API.
 *
 * @param message The message from the API model.
 */
data class ResponseChoice(
    val message: ResponseMessage,
)
