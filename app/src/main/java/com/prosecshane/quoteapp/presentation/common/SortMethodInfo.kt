package com.prosecshane.quoteapp.presentation.common

import com.prosecshane.quoteapp.domain.model.Quote

/**
 * Data class that contains all the information about each sorting method.
 *
 * @param id ID representing the method. Used fro storage in SharedPreferences.
 * @param sortMethod Value of the [SortMethod] enum class.
 * @param nameStringId ID of the string that contains the name for the method.
 * @param function The function that represents the method.
 * @param buttonId ID of the button that sets the sorting method to the given one.
 */
data class SortMethodInfo(
    val id: Int,
    val sortMethod: SortMethod,
    val nameStringId: Int,
    val function: (List<Quote>) -> List<Quote>,
    var buttonId: Int,
)
