package com.prosecshane.quoteapp.data.sharedpreferences

/**
 * Singleton object that stores constants for working with SharedPreferences.
 */
object SPConstants {
    /**
     * Filename where values will be stored.
     */
    const val filename = "quote_app_sp"

    /**
     * Keys for getting certain values used in the app.
     */
    const val askWhenSwipedKey = "quote_app_ask_when_swiped"
    const val askWhenInQuoteKey = "quote_app_ask_when_in_quote"
    const val keywordsKey = "quote_app_keywords"
    const val sortKey = "quote_app_sort_method"
}
