package com.prosecshane.quoteapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.domain.network.GenerationStatus
import com.prosecshane.quoteapp.presentation.viewmodel.KeywordsViewModel
import com.prosecshane.quoteapp.presentation.viewmodel.QuoteAppViewModel
import com.prosecshane.quoteapp.utils.setOnTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A [Fragment] that provides the UI to generate a quote.
 */
@AndroidEntryPoint
class WriteFragment : Fragment() {
    /**
     * ViewModel used specifically for this fragment.
     * Remembers the entered keywords.
     */
    private val keywordsViewModel: KeywordsViewModel by activityViewModels()

    /**
     * ViewModel used in the activity.
     * Get requests to generate a quote and updates the UI
     * depending on the status of the quote generation.
     */
    private val quoteAppViewModel: QuoteAppViewModel by activityViewModels()

    /**
     * Contains information about what the last [GenerationStatus] was.
     * Used to create smooth status screen transitions.
     */
    private var lastGenerationStatus: GenerationStatus? = null

    /**
     * The required onCreateView function.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write, container, false)
    }

    /**
     * This function connects ViewModels' information to fragment's UI
     * to keep it updated. It also binds the button to do its actions
     * and makes the [EditText] remember its value through the ViewModel.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val generateButton: MaterialButton = view.findViewById(R.id.write_generate)
        val keywordsText: EditText = view.findViewById(R.id.write_keywords)

        val statusScreen = view.findViewById<ConstraintLayout>(R.id.write_status)
        statusScreen.visibility = View.INVISIBLE

        generateButton.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    quoteAppViewModel.generateQuote(keywordsViewModel.keywords.value)
                }
            }
        }

        keywordsText.setText(keywordsViewModel.keywords.value)
        keywordsText.setOnTextChangedListener { newValue ->
            keywordsViewModel.setKeywords(newValue)
        }
        keywordsText.setOnEditorActionListener { _, aid, _ ->
            if (aid == EditorInfo.IME_ACTION_SEND) {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        quoteAppViewModel.generateQuote(keywordsViewModel.keywords.value)
                    }
                }
                return@setOnEditorActionListener true
            }
            false
        }

        lifecycleScope.launch {
            quoteAppViewModel.generationStatus.collect {
                changeStatusScreen(it, statusScreen)
                showStatusScreen(it, statusScreen, listOf(generateButton, keywordsText),
                    compareGenerationStatuses(lastGenerationStatus, it)
                )
                lastGenerationStatus = it
            }
        }
    }

    /**
     * Compares the two [GenerationStatus] values and determines
     * whether to make the status screen transition.
     *
     * @param last The previous [GenerationStatus] before the switch.
     * @param current The current [GenerationStatus] after the switch.
     * @return "true" if status screen transition is required, "false" otherwise.
     */
    private fun compareGenerationStatuses(
        last: GenerationStatus?, current: GenerationStatus
    ): Boolean {
        if (last == null)
            return false
        if (last == GenerationStatus.None && current != GenerationStatus.None)
            return true
        if (last != GenerationStatus.None && current == GenerationStatus.None)
            return true
        return false
    }

    /**
     * Prepares the status screen depending on the current [GenerationStatus].
     *
     * @param generationStatus The current [GenerationStatus].
     * @param screen The status screen to prepare.
     */
    private fun changeStatusScreen(generationStatus: GenerationStatus, screen: ConstraintLayout) {
        when (generationStatus) {
            GenerationStatus.InProgress -> {
                screen.findViewById<ProgressBar>(R.id.write_status_progress).visibility =
                    View.VISIBLE
                screen.findViewById<ImageView>(R.id.write_status_icon).visibility = View.INVISIBLE
                screen.findViewById<TextView>(R.id.write_status_error_message).visibility =
                    View.INVISIBLE

                screen.findViewById<TextView>(R.id.write_status_text).let {
                    it.text = getString(R.string.write_status_in_progress)
                    it.setTextColor(ContextCompat.getColor(requireContext(), R.color.divider))
                }
            }

            is GenerationStatus.Error -> {
                screen.findViewById<ProgressBar>(R.id.write_status_progress).visibility =
                    View.INVISIBLE
                screen.findViewById<ImageView>(R.id.write_status_icon).let {
                    it.visibility = View.VISIBLE
                    it.setImageResource(R.drawable.close)
                    it.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
                }
                screen.findViewById<TextView>(R.id.write_status_error_message).let{
                    it.visibility = View.VISIBLE
                    it.text = generationStatus.message
                }

                screen.findViewById<TextView>(R.id.write_status_text).let {
                    it.text = getString(R.string.write_status_error)
                    it.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }
            }

            is GenerationStatus.Success -> {
                screen.findViewById<ProgressBar>(R.id.write_status_progress).visibility =
                    View.INVISIBLE
                screen.findViewById<ImageView>(R.id.write_status_icon).let {
                    it.visibility = View.VISIBLE
                    it.setImageResource(R.drawable.done)
                    it.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                }
                screen.findViewById<TextView>(R.id.write_status_error_message).visibility =
                    View.INVISIBLE

                screen.findViewById<TextView>(R.id.write_status_text).let {
                    it.text = getString(R.string.write_status_success)
                    it.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }
            }

            GenerationStatus.None -> {}
        }
    }

    /**
     * Shows and hides the status screen with or without animation.
     * Disables or enables certain Views depending on whether the status screen is shown or not.
     *
     * @param generationStatus The current [GenerationStatus].
     * @param statusScreen The status screen to show or hide.
     * @param intractableViews The Views that need to be enabled or disabled.
     * @param withAnimation Information whether the status screen needs the animation or not.
     */
    private fun showStatusScreen(
        generationStatus: GenerationStatus,
        statusScreen: ConstraintLayout,
        intractableViews: List<View>,
        withAnimation: Boolean,
    ) {
        if (generationStatus != GenerationStatus.None) {
            statusScreen.visibility = View.VISIBLE
            if (withAnimation) { statusScreen.startAnimation(
                    AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
            ) }
        } else {
            if (withAnimation) { statusScreen.startAnimation(
                    AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
            ) }
            statusScreen.visibility = View.INVISIBLE
        }
        for (view in intractableViews) {
            view.isEnabled = (generationStatus == GenerationStatus.None)
        }
    }
}
