package com.prosecshane.quoteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * A [ViewModel] used in the Write Fragment. Contains
 * state holders for the fragment.
 */
@HiltViewModel
class WriteViewModel @Inject constructor() : ViewModel() {
    /**
     * Private mutable and public immutable [StateFlow]s that
     * contain a string of keywords typed in fragment.
     */
    private val _keywords = MutableStateFlow("")
    val keywords: StateFlow<String> = _keywords.asStateFlow()


    /**
     * Sets the value for keywords state holder.
     *
     * @param input New value to set.
     */
    fun setKeywords(input: String) {
        _keywords.update { input }
    }
}
