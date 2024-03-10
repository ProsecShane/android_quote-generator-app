package com.prosecshane.quoteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prosecshane.quoteapp.data.sharedpreferences.SPConstants
import com.prosecshane.quoteapp.domain.sharedpreferences.SPApi
import com.prosecshane.quoteapp.utils.neatString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A [ViewModel] used to store keywords.
 */
@HiltViewModel
class KeywordsViewModel @Inject constructor(
    private val spApi: SPApi,
) : ViewModel() {
    /**
     * Private mutable and public immutable [StateFlow]s that
     * contain a string of keywords typed in fragment.
     */
    private val _keywords = MutableStateFlow("")
    val keywords: StateFlow<String> = _keywords.asStateFlow()

    /**
     * Getting initial value for the StateFlow.
     */
    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _keywords.update { spApi.get(SPConstants.keywordsKey, "") }
            }
        }
    }

    /**
     * Sets the value for keywords state holder.
     *
     * @param input New value to set.
     */
    fun setKeywords(input: String) {
        val neatInput = neatString(input)
        _keywords.update { neatInput }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                spApi.set(SPConstants.keywordsKey, neatInput)
            }
        }
    }
}
