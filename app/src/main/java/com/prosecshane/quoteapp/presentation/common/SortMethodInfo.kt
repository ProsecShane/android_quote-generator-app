package com.prosecshane.quoteapp.presentation.common

import com.prosecshane.quoteapp.domain.model.Quote

data class SortMethodInfo(
    val id: Int,
    val sortMethod: SortMethod,
    val nameStringId: Int,
    val function: (List<Quote>) -> List<Quote>,
    var buttonId: Int,
)
