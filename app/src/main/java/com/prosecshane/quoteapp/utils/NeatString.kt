package com.prosecshane.quoteapp.utils

/**
 * Utility function that makes the string clean.
 *
 * @param input The string to clean.
 * @return Clean string.
 */
fun neatString(input: String) = input.trim().replace("\\s+".toRegex(), " ")
