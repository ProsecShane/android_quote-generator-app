package com.prosecshane.quoteapp.utils

import android.content.Context
import android.widget.Toast

/**
 * Utility function to shorten calls to create Toast messages.
 *
 * @param context [Context] in which the messages is going to be created.
 * @param message The string for the message that is going to be shown.
 */
fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
