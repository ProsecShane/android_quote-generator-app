package com.prosecshane.quoteapp.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.domain.model.Quote
import com.prosecshane.quoteapp.utils.millisToFormattedDate

/**
 * ViewHolder that contains information about a [Quote]
 * and that is used in RecyclerView.
 */
class QuoteViewHolder(quoteView: View) : RecyclerView.ViewHolder(quoteView) {
    /**
     * Elements of the item that need to be styled according to the given [Quote] instance.
     */
    private val content: TextView = quoteView.findViewById(R.id.item_content)
    private val date: TextView = quoteView.findViewById(R.id.item_date)
    private val description = quoteView.context.getString(R.string.item_format)

    /**
     * Styles the view.
     *
     * @param quote The basis for setting values in the view.
     */
    fun bind(quote: Quote) {
        content.text = if (quote.content != "") quote.content else "No quote"
        date.text = description.format(
            millisToFormattedDate(quote.created),
            if (quote.keywords != "") quote.keywords else "No keywords"
        )
    }
}
