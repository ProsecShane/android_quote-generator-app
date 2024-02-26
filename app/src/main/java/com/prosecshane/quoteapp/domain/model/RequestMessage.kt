package com.prosecshane.quoteapp.domain.model

/**
 * A part of the body for an API call.
 *
 * @param role String of the role that the AI model needs to execute.
 * @param content String of the prompt for the AI model.
 */
data class RequestMessage(
    val role: String,
    val content: String,
)
