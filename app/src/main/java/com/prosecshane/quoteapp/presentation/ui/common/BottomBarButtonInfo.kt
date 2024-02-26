package com.prosecshane.quoteapp.presentation.ui.common

/**
 * Data class that creates an instance, containing information
 * about one of the bottom bar buttons.
 *
 * @param type What fragment would the button lead to by pressing it.
 * @param outlinedDrawableId ID of the drawable for when button can be pressed.
 * @param filledDrawableId ID of the drawable for when the button was pressed.
 */
data class BottomBarButtonInfo(
    val type: FragmentVariant,
    val outlinedDrawableId: Int,
    val filledDrawableId: Int,
)
