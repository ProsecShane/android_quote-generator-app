package com.prosecshane.quoteapp.presentation.common

import com.prosecshane.quoteapp.R

/**
 * List of all the information regarding sorting methods.
 */
val sortMethodsInfo = listOf(
    SortMethodInfo(
        id = 0,
        sortMethod = SortMethod.ByDateDes,
        nameStringId = R.string.sort_date_desc,
        function = { ql -> ql.sortedByDescending { it.created } },
        buttonId = R.id.sort_date_desc,
    ),
    SortMethodInfo(
        id = 1,
        sortMethod = SortMethod.ByDateAsc,
        nameStringId = R.string.sort_date_asc,
        function = { ql -> ql.sortedBy { it.created } },
        buttonId = R.id.sort_date_asc,
    ),
    SortMethodInfo(
        id = 2,
        sortMethod = SortMethod.ByQuoteAsc,
        nameStringId = R.string.sort_quote_asc,
        function = { ql -> ql.sortedBy { it.content } },
        buttonId = R.id.sort_quote_asc,
    ),
    SortMethodInfo(
        id = 3,
        sortMethod = SortMethod.ByQuoteDes,
        nameStringId = R.string.sort_quote_desc,
        function = { ql -> ql.sortedByDescending { it.content } },
        buttonId = R.id.sort_quote_desc,
    ),
    SortMethodInfo(
        id = 4,
        sortMethod = SortMethod.ByKwAsc,
        nameStringId = R.string.sort_kw_asc,
        function = { ql -> ql.sortedBy { it.keywords } },
        buttonId = R.id.sort_kw_asc,
    ),
    SortMethodInfo(
        id = 5,
        sortMethod = SortMethod.ByKwDes,
        nameStringId = R.string.sort_kw_desc,
        function = { ql -> ql.sortedByDescending { it.keywords } },
        buttonId = R.id.sort_kw_desc,
    ),
)
