package com.prosecshane.quoteapp.domain.model.util

/**
 * Singleton object for storing constants used to create the body for an API call.
 */
object RequestConstants {
    /**
     * Constant that contains the string of a default error message
     * in case the AI model refuses to generate a response.
     */
    const val errorMessage = "_ERROR"

    /**
     * A function that formats the prompt string based on given information.
     * Formats the input so as to not make kind of an injection attack
     * using prompts by giving the AI model extra instructions.
     *
     * @param input Keywords used in the API call.
     * @return Formatted string of the full instruction for the AI model.
     * Contains the given keywords and the default error message.
     */
    fun formatPrompt(input: String): String {
        val filteredInput = input.replace(Regex("()\"'."), "")
        return "Create a quote based on these keywords: \"$filteredInput\". Write only the quote, nothing else. If you can't generate the quote, just type \"$errorMessage\""
    }
}
