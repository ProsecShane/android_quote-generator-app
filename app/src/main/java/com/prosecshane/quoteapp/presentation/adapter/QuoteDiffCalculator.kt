package com.prosecshane.quoteapp.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.prosecshane.quoteapp.domain.model.Quote

class QuoteDiffCalculator : DiffUtil.ItemCallback<Quote>() {
    override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return (oldItem.content == newItem.content &&
                oldItem.keywords == newItem.keywords &&
                oldItem.created == newItem.created)
    }
}
