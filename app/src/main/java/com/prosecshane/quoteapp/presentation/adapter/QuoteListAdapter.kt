package com.prosecshane.quoteapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.domain.model.Quote

class QuoteListAdapter(
    diffCalculator: QuoteDiffCalculator,
    private val onItemClickCallback: (Quote) -> Unit,
) : ListAdapter<Quote, QuoteViewHolder>(diffCalculator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.quote_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        holder.bind(quote)
        holder.itemView.setOnClickListener { onItemClickCallback(quote) }
    }

    public override fun getItem(position: Int): Quote = super.getItem(position)

    fun redrawViewHolder(position: Int) {
        notifyItemChanged(position)
    }
}
