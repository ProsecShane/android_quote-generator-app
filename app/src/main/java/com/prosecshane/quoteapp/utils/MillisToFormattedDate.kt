package com.prosecshane.quoteapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility function to format a timestamp from [Long] to [String].
 *
 * @param millis Timestamp represented as a [Long] variable.
 * @return Timestamp represented as a [String] variable.
 */
fun millisToFormattedDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
    val date = Date(millis)
    return dateFormat.format(date)
}
