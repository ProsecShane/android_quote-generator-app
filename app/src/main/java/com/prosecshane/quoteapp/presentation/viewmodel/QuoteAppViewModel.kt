package com.prosecshane.quoteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prosecshane.quoteapp.domain.model.Quote
import com.prosecshane.quoteapp.domain.network.GenerationStatus
import com.prosecshane.quoteapp.domain.repository.QuoteRepository
import com.prosecshane.quoteapp.presentation.ui.common.FragmentVariant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
import javax.inject.Inject

/**
 * A ViewModel used in the Main Activity and some of its Fragments.
 * Contains state holders and calls to the repository.
 *
 * @param repository A repository that works with the data concerning the app.
 */
@HiltViewModel
class QuoteAppViewModel @Inject constructor(
    private val repository: QuoteRepository,
) : ViewModel() {
    /**
     * Private mutable and public immutable state flows containing
     * a stack of shown fragments in certain order.
     */
    private val _lastPressedStack = MutableStateFlow(mutableListOf(FragmentVariant.Write))
    val lastPressedStack: StateFlow<List<FragmentVariant>> = _lastPressedStack.asStateFlow()

    /**
     * Private mutable and public immutable state flows containing
     * the current quote that needs to be displayed in the Quote Fragment.
     */
    private val _currentQuote = MutableStateFlow(Quote(
        content = "No quote",
        keywords = "No keywords",
    ))
    val currentQuote: StateFlow<Quote> = _currentQuote.asStateFlow()

    /**
     * Private mutable and public immutable state flows containing
     * the current status of quote generation.
     */
    private val _generationStatus: MutableStateFlow<GenerationStatus> =
        MutableStateFlow(GenerationStatus.None)
    val generationStatus: StateFlow<GenerationStatus> = _generationStatus.asStateFlow()

    /**
     * Adds a new entry to the top of the fragment stack.
     *
     * @param newFragmentVariant A new [FragmentVariant] entry to add.
     */
    fun addToLastPressedStack(newFragmentVariant: FragmentVariant) {
        _lastPressedStack.update {
            (it + listOf(newFragmentVariant)).toMutableList()
        }
    }

    /**
     * Removes the top (last) entry of the fragment stack.
     */
    fun popLastPressedStack() {
        _lastPressedStack.update { it.dropLast(1).toMutableList() }
    }

    /**
     * Sets a new value to the current quote stateflow.
     *
     * @param quote New [Quote] value.
     */
    fun setQuote(quote: Quote) {
        _currentQuote.update { quote }
    }

    /**
     * Requests the repository for a quote to be generated.
     * Changes the ViewModel's state holders depending on the result of the request.
     * If a quote is successfully generated, then it gets saved.
     *
     * @param keywords Keywords used for quote generation.
     */
    fun generateQuote(keywords: String) {
        viewModelScope.launch {
            repository.generateQuote(keywords).collect {
                setGenerationStatus(it)
                when (it) {
                    is GenerationStatus.Error -> {
                        viewModelScope.launch { withContext(Dispatchers.IO) {
                            sleep(3500)
                            setGenerationStatus(GenerationStatus.None)
                        } }
                    }
                    is GenerationStatus.Success -> {
                        viewModelScope.launch { withContext(Dispatchers.IO) {
                            val quote = Quote(content = it.quote, keywords = keywords)

                            val startedWriting = System.currentTimeMillis()
                            repository.saveQuote(quote)
                            val writingTime = System.currentTimeMillis() - startedWriting
                            if (writingTime < 2000) sleep(2000 - writingTime)

                            setQuote(quote)
                            addToLastPressedStack(FragmentVariant.Quote)
                            setGenerationStatus(GenerationStatus.None)
                        } }
                    }
                    GenerationStatus.InProgress -> {}
                    GenerationStatus.None -> {}
                }
            }
        }
    }

    /**
     * Sets a new generation status.
     *
     * @param newGenerationStatus New value for the [GenerationStatus] state holder.
     */
    fun setGenerationStatus(newGenerationStatus: GenerationStatus) {
        _generationStatus.update { newGenerationStatus }
    }
}
