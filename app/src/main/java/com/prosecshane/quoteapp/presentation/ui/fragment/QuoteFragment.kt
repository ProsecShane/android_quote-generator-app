package com.prosecshane.quoteapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.prosecshane.quoteapp.R
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
                contentText.text = it.content
                keywordsText.text = it.keywords
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
            Toast.makeText(requireContext(), "Not yet implemented!", Toast.LENGTH_SHORT).show()
            TODO("Implement correct usage")
        }

        copyKeywordsButton.setOnClickListener {
            copyToClipboard(
                context = requireContext(),
                input = quoteAppViewModel.currentQuote.value.keywords
            )
        }

        removeButton.setOnClickListener {
            Toast.makeText(requireContext(), "Not yet implemented!", Toast.LENGTH_SHORT).show()
            TODO("Implement correct usage")
        }
    }
}
