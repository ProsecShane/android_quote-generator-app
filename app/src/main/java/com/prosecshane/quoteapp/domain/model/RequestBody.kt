package com.prosecshane.quoteapp.domain.model

/**
 * Body of the request. Used to create a body for an API call.
 * Most parameters are set automatically.
 *
 * @param messages Contains the prompt for the AI model to parse.
 */
data class RequestBody(
    val messages: List<RequestMessage>,
    val max_tokens: Int,
    val prompt_truncate_len: Int,
    val temperature: Int,
    val top_p: Int,
    val frequency_penalty: Int,
    val presence_penalty: Int,
    val n: Int,
    val response_format: RequestResponseFormat,
    val stream: Boolean,
    val model: String,
    val context_length_exceeded_behavior: String,
)
