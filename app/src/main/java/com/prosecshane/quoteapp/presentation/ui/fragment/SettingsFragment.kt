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
 * Not yet implemented!
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val localDataViewModel: LocalDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

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

    private fun bindClearButton(clearButton: MaterialButton) {
        clearButton.setOnClickListener {
            confirmClear {
                localDataViewModel.clearQuotes()
            }
        }
    }

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
