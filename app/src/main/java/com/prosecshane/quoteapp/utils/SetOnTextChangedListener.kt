package com.prosecshane.quoteapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * An extension function for [EditText] that simplifies
 * setting a "text changed" listener.
 *
 * @param callback Callback that processes the new value of [EditText].
 */
fun EditText.setOnTextChangedListener(callback: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(newValue: CharSequence?, p1: Int, p2: Int, p3: Int) {
            callback((newValue?: "").toString())
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
    })
}
