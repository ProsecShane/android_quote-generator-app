package com.prosecshane.quoteapp.domain.model.util

import com.prosecshane.quoteapp.domain.model.RequestBody
import com.prosecshane.quoteapp.domain.model.RequestMessage
import com.prosecshane.quoteapp.domain.model.RequestResponseFormat

/**
 * Utility function that creates the body for an API call.
 *
 * @param keywords Keywords used in the API call.
 * @return Instance of the [RequestBody] class with
 * many pre-set variables.
 */
fun defaultRequestBody(keywords: String): RequestBody {
    return RequestBody(
        messages = listOf(RequestMessage(
            role = "user",
            content = RequestConstants.formatPrompt(keywords),
        )),
        max_tokens = 400,
        prompt_truncate_len = 1000,
        temperature = 1,
        top_p = 1,
        frequency_penalty = 0,
        presence_penalty = 0,
        n = 1,
        response_format = RequestResponseFormat(type = "text"),
        stream = false,
        model = "accounts/fireworks/models/mistral-7b-instruct-4k",
        context_length_exceeded_behavior = "error",
    )
}
