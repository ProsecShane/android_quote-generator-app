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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.domain.model.Quote
import com.prosecshane.quoteapp.presentation.adapter.QuoteDiffCalculator
import com.prosecshane.quoteapp.presentation.adapter.QuoteListAdapter
import com.prosecshane.quoteapp.presentation.adapter.SwipeToDeleteCallback
import com.prosecshane.quoteapp.presentation.common.sortMethodsInfo
import com.prosecshane.quoteapp.presentation.ui.common.FragmentVariant
import com.prosecshane.quoteapp.presentation.viewmodel.LocalDataViewModel
import com.prosecshane.quoteapp.presentation.viewmodel.QuoteAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A [Fragment] containing all the saved quotes.
 */
@AndroidEntryPoint
class ListFragment : Fragment() {
    /**
     * A ViewModel that deals with the data stored locally.
     * Used to get sort method and quotes and set or delete quotes.
     */
    private val localDataViewModel: LocalDataViewModel by activityViewModels()

    /**
     * A ViewModel that goes through the entire app.
     * Used for screen transfer by opening quotes from the list.
     */
    private val quoteAppViewModel: QuoteAppViewModel by activityViewModels()

    /**
     * Obligatory function that sets the layout for the Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    /**
     * Main function that sets up logic for the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.list_list)
        val sortButton: ImageButton = view.findViewById(R.id.list_sort_button)
        val emptyLabel: TextView = view.findViewById(R.id.list_no_items)

        val onItemClickCallback: (Quote) -> Unit  = { quote ->
            quoteAppViewModel.setQuote(quote)
            quoteAppViewModel.addToLastPressedStack(FragmentVariant.Quote)
        }
        val onItemSwipedCallback: (Quote) -> Unit = { quote ->
            val onDeleteCallback: () -> Unit = { localDataViewModel.deleteQuote(quote) }
            val onCancelCallback: () -> Unit = {
                val adapter = recyclerView.adapter as QuoteListAdapter
                adapter.redrawViewHolder(localDataViewModel.quotes.value.indexOf(quote))
            }

            if (localDataViewModel.askWhenSwiped.value) {
                localDataViewModel.confirmDeletion(
                    requireContext(),
                    onDeleteCallback,
                    onCancelCallback,
                    onCancelCallback,
                )
            } else {
                onDeleteCallback()
            }
        }
        setUpRecyclerView(
            recyclerView,
            onItemClickCallback,
            onItemSwipedCallback,
            listOf(),
        )

        sortButton.setOnClickListener {
            chooseSort()
        }

        lifecycleScope.launch {
            localDataViewModel.updateQuotes()
            localDataViewModel.quotes.collect { ql ->
                val sortMethod = localDataViewModel.sortMethod.value
                val sortFunction = sortMethodsInfo.first { it.sortMethod == sortMethod }.function
                val adapter = recyclerView.adapter as QuoteListAdapter
                adapter.submitList(sortFunction(ql).toList())
                emptyLabel.visibility = if (ql.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            localDataViewModel.sortMethod.collect { sm ->
                val sortFunction = sortMethodsInfo.first { it.sortMethod == sm }.function
                val adapter = recyclerView.adapter as QuoteListAdapter
                adapter.submitList(sortFunction(localDataViewModel.quotes.value).toList())
                emptyLabel.visibility =
                    if (localDataViewModel.quotes.value.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * A function that sets up the RecyclerView.
     *
     * @param recyclerView The [RecyclerView] to set up.
     * @param onItemClickCallback Callback invoked by item click.
     * @param onItemSwipedCallback Callback invoked by item swipe.
     * @param initialList The initial value for items of the [RecyclerView].
     */
    private fun setUpRecyclerView(
        recyclerView: RecyclerView,
        onItemClickCallback: (Quote) -> Unit,
        onItemSwipedCallback: (Quote) -> Unit,
        initialList: List<Quote>,
    ) {
        val diffCalculator = QuoteDiffCalculator()
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as QuoteListAdapter
                val quote = adapter.getItem(viewHolder.adapterPosition)
                onItemSwipedCallback(quote)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = QuoteListAdapter(diffCalculator, onItemClickCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        (recyclerView.adapter as QuoteListAdapter).submitList(initialList.toList())
    }

    /**
     * Function that opens up the bottom sheet that allows to pick a sort method.
     */
    private fun chooseSort() {
        val bottomSheetFragment = SortBottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }
}
