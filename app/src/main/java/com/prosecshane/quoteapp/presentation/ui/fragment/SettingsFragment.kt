package com.prosecshane.quoteapp.presentation.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.presentation.viewmodel.LocalDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A [Fragment] containing settings for the app.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {
    /**
     * A ViewModel that deals with all the data stored locally.
     * Used to set the settings and clear quotes if requested.
     */
    private val localDataViewModel: LocalDataViewModel by activityViewModels()

    /**
     * Obligatory function to set the layout for the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    /**
     * Function that sets up all the logic in the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipedOn: RadioButton = view.findViewById(R.id.settings_swiped_on)
        val swipedOff: RadioButton = view.findViewById(R.id.settings_swiped_off)

        val inQuoteOn: RadioButton = view.findViewById(R.id.settings_in_quote_on)
        val inQuoteOff: RadioButton = view.findViewById(R.id.settings_in_quote_off)

        val clearButton: MaterialButton = view.findViewById(R.id.settings_clear)

        bindRadioButtons(swipedOn, swipedOff, inQuoteOn, inQuoteOff)
        bindClearButton(clearButton)

        lifecycleScope.launch {
            localDataViewModel.askWhenSwiped.collect {
                swipedOn.isChecked = it
                swipedOff.isChecked = !it
            }
        }

        lifecycleScope.launch {
            localDataViewModel.askWhenInQuote.collect {
                inQuoteOn.isChecked = it
                inQuoteOff.isChecked = !it
            }
        }
    }

    /**
     * Binds the [RadioButton]s logic.
     *
     * @param swipedOn The button that makes the user confirm the deletion on swipe.
     * @param swipedOff The button that disables the user's confirmation of the deletion on swipe.
     * @param inQuoteOff The button that makes the user confirm the deletion when in quote.
     * @param inQuoteOn The button that disables the user's confirmation of the deletion when in quote.
     */
    private fun bindRadioButtons(
        swipedOn: RadioButton,
        swipedOff: RadioButton,
        inQuoteOn: RadioButton,
        inQuoteOff: RadioButton,
    ) {
        swipedOn.setOnClickListener { localDataViewModel.setAskWhenSwiped(true) }
        swipedOff.setOnClickListener { localDataViewModel.setAskWhenSwiped(false) }

        inQuoteOn.setOnClickListener { localDataViewModel.setAskWhenInQuote(true) }
        inQuoteOff.setOnClickListener { localDataViewModel.setAskWhenInQuote(false) }
    }

    /**
     * Binds the Button that clears all the quotes.
     */
    private fun bindClearButton(clearButton: MaterialButton) {
        clearButton.setOnClickListener {
            confirmClear {
                localDataViewModel.clearQuotes()
            }
        }
    }

    /**
     * First confirmation of the quotes' clear.
     *
     * @param onDeleteCallback Callback that clears all the quotes.
     */
    private fun confirmClear(
        onDeleteCallback: () -> Unit,
    ) {
        val title = requireContext().getString(R.string.clear_title)
        val description = requireContext().getString(R.string.clear_description)
        val delete = requireContext().getString(R.string.delete_yes)
        val cancel = requireContext().getString(R.string.delete_no)

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(delete) { _, _ -> confirmClear2(onDeleteCallback) }
            .setNegativeButton(cancel) { _, _ -> }
            .setOnCancelListener { }
            .create()
            .show()
    }

    /**
     * Second confirmation of the quotes' clear.
     *
     * @param onDeleteCallback Callback that clears all the quotes.
     */
    private fun confirmClear2(
        onDeleteCallback: () -> Unit,
    ) {
        val title = requireContext().getString(R.string.clear_title)
        val description = requireContext().getString(R.string.clear_description_2)
        val delete = requireContext().getString(R.string.delete_yes)
        val cancel = requireContext().getString(R.string.delete_no)

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(delete) { _, _ -> onDeleteCallback() }
            .setNegativeButton(cancel) { _, _ -> }
            .setOnCancelListener { }
            .create()
            .show()
    }
}
