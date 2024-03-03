package com.prosecshane.quoteapp.utils

fun neatString(input: String) = input.trim().replace("\\s+".toRegex(), " ")
