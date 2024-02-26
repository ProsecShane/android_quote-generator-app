package com.prosecshane.quoteapp.utils

import android.content.ClipboardManager
import android.content.Context

/**
 * Utility function to simplify copying a string to clipboard.
 *
 * @param context Context to get the clipboard system service.
 * @param input A string to be copied to clipboard.
 */
fun copyToClipboard(
    context: Context,
    input: String,
) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = android.content.ClipData.newPlainText("text", input)
    clipboardManager.setPrimaryClip(clipData)
}
