package com.prosecshane.quoteapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.domain.model.Quote

/**
 * ListAdapter implementation used for RecyclerView.
 *
 * @param diffCalculator Instance of the [QuoteDiffCalculator] to compare objects.
 * @param onItemClickCallback Callback invoked by pressing an item.
 */
class QuoteListAdapter(
    diffCalculator: QuoteDiffCalculator,
    private val onItemClickCallback: (Quote) -> Unit,
) : ListAdapter<Quote, QuoteViewHolder>(diffCalculator) {
    /**
     * Implementation of the function that sets the layout for each item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.quote_layout, parent, false)
        )
    }

    /**
     * Implementation of the function that binds every item with the given
     * ViewHolder and creates logic for each item (sets onLClickListeners).
     */
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        holder.bind(quote)
        holder.itemView.setOnClickListener { onItemClickCallback(quote) }
    }

    /**
     * Public implementation of the [getItem] function.
     */
    public override fun getItem(position: Int): Quote = super.getItem(position)

    /**
     * Redraws a given item to its initial state.
     */
    fun redrawViewHolder(position: Int) {
        notifyItemChanged(position)
    }
}
