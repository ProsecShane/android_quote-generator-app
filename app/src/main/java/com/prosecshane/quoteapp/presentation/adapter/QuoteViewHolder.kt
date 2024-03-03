package com.prosecshane.quoteapp.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.domain.model.Quote
import com.prosecshane.quoteapp.utils.millisToFormattedDate

class QuoteViewHolder(quoteView: View) : RecyclerView.ViewHolder(quoteView) {
    private val content: TextView = quoteView.findViewById(R.id.item_content)
    private val date: TextView = quoteView.findViewById(R.id.item_date)
    private val description = quoteView.context.getString(R.string.item_format)

    fun bind(quote: Quote) {
        content.text = if (quote.content != "") quote.content else "No quote"
        date.text = description.format(
            millisToFormattedDate(quote.created),
            if (quote.keywords != "") quote.keywords else "No keywords"
        )
    }
}
