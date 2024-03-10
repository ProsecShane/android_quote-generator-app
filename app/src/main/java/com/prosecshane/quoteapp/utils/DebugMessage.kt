package com.prosecshane.quoteapp.utils

import android.util.Log

const val DebugTag = "QuoteAppDebug"

/**
 * Utility functions that simplifies logging a message with a Debug tag.
 *
 * @param input A message to log.
 */
fun debugMessage(input: String) {
    Log.d(DebugTag, input)
}
