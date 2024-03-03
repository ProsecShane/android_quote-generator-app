package com.prosecshane.quoteapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.domain.model.Quote
import com.prosecshane.quoteapp.presentation.ui.common.FragmentVariant
import com.prosecshane.quoteapp.presentation.viewmodel.KeywordsViewModel
import com.prosecshane.quoteapp.presentation.viewmodel.LocalDataViewModel
import com.prosecshane.quoteapp.presentation.viewmodel.QuoteAppViewModel
import com.prosecshane.quoteapp.utils.copyToClipboard
import com.prosecshane.quoteapp.utils.millisToFormattedDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A [Fragment] that displays all the information about a given quote.
 */
@AndroidEntryPoint
class QuoteFragment : Fragment() {
    /**
     * The ViewModel used in the activity. Used to get what quote to display.
     */
    private val quoteAppViewModel: QuoteAppViewModel by activityViewModels()

    /**
     * The ViewModel used for local data. Used to delete a quote (if necessary)
     * and get settings about deletion confirmation.
     */
    private val localDataViewModel: LocalDataViewModel by activityViewModels()

    /**
     * The ViewModel used for keywords. Sets them if regeneration was requested.
     */
    private val keywordsViewModel: KeywordsViewModel by activityViewModels()

    /**
     * The required onCreateView function.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quote, container, false)
    }

    /**
     * This function connects ViewModel's information to fragment's UI
     * to keep it updated. It also binds all the clickable buttons
     * to do their respective actions. Not fully implemented yet!
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateText: TextView = view.findViewById(R.id.quote_date)
        val contentText: TextView = view.findViewById(R.id.quote_content)
        val keywordsText: TextView = view.findViewById(R.id.quote_keywords)

        val backButton: MaterialButton = view.findViewById(R.id.quote_back)
        val copyContentButton: MaterialButton = view.findViewById(R.id.quote_copy_content)
        val regenerateButton: MaterialButton = view.findViewById(R.id.quote_regenerate)
        val copyKeywordsButton: ImageButton = view.findViewById(R.id.quote_copy_keywords)
        val removeButton: MaterialButton = view.findViewById(R.id.quote_delete)

        lifecycleScope.launch {
            quoteAppViewModel.currentQuote.collect {
                val formattedDate = millisToFormattedDate(it.created)
                dateText.text = getString(R.string.quote_date, formattedDate)
                contentText.text = if (it.content != "") it.content else "No Quote."
                keywordsText.text =  if (it.keywords != "") it.keywords else "No Keywords"
            }
        }

        backButton.setOnClickListener {
            quoteAppViewModel.popLastPressedStack()
        }

        copyContentButton.setOnClickListener {
            copyToClipboard(
                context = requireContext(),
                input = quoteAppViewModel.currentQuote.value.content
            )
        }

        regenerateButton.setOnClickListener {
            keywordsViewModel.setKeywords(quoteAppViewModel.currentQuote.value.keywords)
            quoteAppViewModel.addToLastPressedStack(FragmentVariant.Write)
        }

        copyKeywordsButton.setOnClickListener {
            copyToClipboard(
                context = requireContext(),
                input = quoteAppViewModel.currentQuote.value.keywords
            )
        }

        removeButton.setOnClickListener {
            val onDeleteCallback: () -> Unit = { deleteQuote(quoteAppViewModel.currentQuote.value) }
            if (localDataViewModel.askWhenInQuote.value) {
                localDataViewModel.confirmDeletion(requireContext(), onDeleteCallback, { }, { })
            } else {
                onDeleteCallback()
            }
        }
    }

    /**
     * A function that deletes a quote and kicks the user from seeing it.
     *
     * @param quote A [Quote] to delete.
     */
    private fun deleteQuote(quote: Quote) {
        localDataViewModel.deleteQuote(quote)
        quoteAppViewModel.setQuote(Quote(content = "No quote", keywords = ""))
        quoteAppViewModel.popLastPressedStack()
    }
}
