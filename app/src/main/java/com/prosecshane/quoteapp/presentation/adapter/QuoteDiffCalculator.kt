package com.prosecshane.quoteapp.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.prosecshane.quoteapp.domain.model.Quote

/**
 * Difference calculator for the RecyclerView.
 * Compares two [Quote] instances.
 */
class QuoteDiffCalculator : DiffUtil.ItemCallback<Quote>() {
    /**
     * Implementation of the function that checks whether
     * two [Quote] instances are the same.
     *
     * @param oldItem First [Quote] instance.
     * @param newItem Second [Quote] instance.
     * @return "True" if they are the same, "False otherwise.
     */
    override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Implementation of the function that checks whether
     * two [Quote] instances have the same contents.
     *
     * @param oldItem First [Quote] instance.
     * @param newItem Second [Quote] instance.
     * @return "True" if they have the same contents, "False otherwise.
     */
    override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return (oldItem.content == newItem.content &&
                oldItem.keywords == newItem.keywords &&
                oldItem.created == newItem.created)
    }
}
