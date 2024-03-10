package com.prosecshane.quoteapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.presentation.common.sortMethodsInfo
import com.prosecshane.quoteapp.presentation.viewmodel.LocalDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * [BottomSheetDialogFragment] implementation that makes the user pick a sort method.
 */
@AndroidEntryPoint
class SortBottomSheetFragment : BottomSheetDialogFragment() {
    /**
     * A ViewModel that deals with locally stored data.
     * Displays the current sort method and sets a new one.
     */
    private val localDataViewModel: LocalDataViewModel by activityViewModels()

    /**
     * Obligatory function that sets the layout for the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sort_choice, container, false)
    }

    /**
     * Function that sets up all of the logic for the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list: LinearLayout = view.findViewById(R.id.sort_choices)

        sortMethodsInfo.forEach { smi ->
            val button: MaterialButton = list.findViewById(smi.buttonId)
            button.text = getString(smi.nameStringId)
            button.setOnClickListener {
                localDataViewModel.setSortMethod(smi.sortMethod)
                dismiss()
            }
        }

        lifecycleScope.launch {
            localDataViewModel.sortMethod.collect { sm ->
                val activeButtonId = sortMethodsInfo.first { it.sortMethod == sm }.buttonId
                sortMethodsInfo.forEach { smi ->
                    val button: MaterialButton = list.findViewById(smi.buttonId)
                    button.icon = if (activeButtonId != button.id) null
                        else AppCompatResources.getDrawable(requireContext(), R.drawable.done)
                }
            }
        }
    }
}
