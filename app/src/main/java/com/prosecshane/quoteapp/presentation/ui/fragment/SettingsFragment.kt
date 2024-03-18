package com.prosecshane.quoteapp.presentation.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
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

        val swiped: ConstraintLayout = view.findViewById(R.id.settings_swiped)
        val swipedMark: CheckBox = view.findViewById(R.id.settings_swiped_mark)

        val inQuote: ConstraintLayout = view.findViewById(R.id.settings_in_quote)
        val inQuoteMark: CheckBox = view.findViewById(R.id.settings_in_quote_mark)

        val clearButton: MaterialButton = view.findViewById(R.id.settings_clear)

        bindCheckableOptions(swiped, swipedMark, inQuote, inQuoteMark)
        bindClearButton(clearButton)

        lifecycleScope.launch {
            localDataViewModel.askWhenSwiped.collect {
                swipedMark.isChecked = it
            }
        }

        lifecycleScope.launch {
            localDataViewModel.askWhenInQuote.collect {
                inQuoteMark.isChecked = it
            }
        }
    }

    /**
     * Binds the options with a CheckBox.
     *
     * @param swiped The option that sets whether the user needs to confirm the deletion on swipe.
     * @param swipedMark The [CheckBox] of the option that sets
     * whether the user needs to confirm the deletion on swipe.
     * @param inQuote The option that sets whether the user needs to confirm the deletion on swipe.
     * @param inQuoteMark The [CheckBox] of the option that sets
     * whether the user needs to confirm the deletion on swipe.
     */
    private fun bindCheckableOptions(
        swiped: ConstraintLayout,
        swipedMark: CheckBox,
        inQuote: ConstraintLayout,
        inQuoteMark: CheckBox,
    ) {
        swiped.setOnClickListener {
            localDataViewModel.setAskWhenSwiped(!localDataViewModel.askWhenSwiped.value)
        }
        swipedMark.setOnClickListener {
            localDataViewModel.setAskWhenSwiped(!localDataViewModel.askWhenSwiped.value)
        }
        inQuote.setOnClickListener {
            localDataViewModel.setAskWhenInQuote(!localDataViewModel.askWhenInQuote.value)
        }
        inQuoteMark.setOnClickListener {
            localDataViewModel.setAskWhenInQuote(!localDataViewModel.askWhenInQuote.value)
        }
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
