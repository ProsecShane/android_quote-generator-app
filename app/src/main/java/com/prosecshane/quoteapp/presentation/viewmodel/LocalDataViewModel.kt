package com.prosecshane.quoteapp.presentation.viewmodel

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.data.sharedpreferences.SPConstants
import com.prosecshane.quoteapp.domain.model.Quote
import com.prosecshane.quoteapp.domain.repository.QuoteRepository
import com.prosecshane.quoteapp.domain.sharedpreferences.SPApi
import com.prosecshane.quoteapp.presentation.common.SortMethod
import com.prosecshane.quoteapp.presentation.common.sortMethodsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocalDataViewModel @Inject constructor(
    private val repository: QuoteRepository,
    private val spApi: SPApi,
) : ViewModel() {
    private val _quotes = MutableStateFlow(listOf<Quote>())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _askWhenSwiped = MutableStateFlow(true)
    val askWhenSwiped: StateFlow<Boolean> = _askWhenSwiped.asStateFlow()

    private val _askWhenInQuote = MutableStateFlow(true)
    val askWhenInQuote: StateFlow<Boolean> = _askWhenInQuote.asStateFlow()

    private val _sortMethod = MutableStateFlow(SortMethod.ByDateDes)
    val sortMethod: StateFlow<SortMethod> = _sortMethod.asStateFlow()

    init {
        updateQuotes()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _askWhenSwiped.update {
                    spApi.get(SPConstants.askWhenSwipedKey, true)
                }
                _askWhenInQuote.update {
                    spApi.get(SPConstants.askWhenInQuoteKey, true)
                }
                _sortMethod.update {
                    val sortMethodId = spApi.get(SPConstants.sortKey, 0)
                    sortMethodsInfo.first { it.id == sortMethodId }.sortMethod
                }
            }
        }
    }

    private fun setQuotes(quotes: List<Quote>) {
        _quotes.update { quotes }
    }

    fun deleteQuote(quote: Quote) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteLocalQuote(quote.id)
                setQuotes(repository.getLocalQuotes())
            }
        }
    }

    fun clearQuotes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.clearLocalQuotes()
                setQuotes(listOf())
            }
        }
    }

    fun updateQuotes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                setQuotes(repository.getLocalQuotes())
            }
        }
    }

    fun setAskWhenSwiped(newValue: Boolean) {
        _askWhenSwiped.update { newValue }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                spApi.set(SPConstants.askWhenSwipedKey, newValue)
            }
        }
    }

    fun setAskWhenInQuote(newValue: Boolean) {
        _askWhenInQuote.update { newValue }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                spApi.set(SPConstants.askWhenInQuoteKey, newValue)
            }
        }
    }

    fun setSortMethod(newValue: SortMethod) {
        _sortMethod.update { newValue }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sortMethodId = sortMethodsInfo.first { it.sortMethod == newValue }.id
                spApi.set(SPConstants.sortKey, sortMethodId)
            }
        }
    }

    fun confirmDeletion(
        context: Context,
        onDeleteCallback: () -> Unit,
        onCancelCallback: () -> Unit,
        onDismissCallback: () -> Unit,
    ) {
        val title = context.getString(R.string.delete_title)
        val description = context.getString(R.string.delete_description)
        val delete = context.getString(R.string.delete_yes)
        val cancel = context.getString(R.string.delete_no)

        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(delete) { _, _ -> onDeleteCallback() }
            .setNegativeButton(cancel) { _, _ -> onCancelCallback() }
            .setOnCancelListener { onDismissCallback() }
            .create()
            .show()
    }
}