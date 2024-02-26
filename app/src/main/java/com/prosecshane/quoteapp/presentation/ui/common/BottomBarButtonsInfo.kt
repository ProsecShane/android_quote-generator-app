package com.prosecshane.quoteapp.presentation.ui.common

import com.prosecshane.quoteapp.R

/**
 * A map that connects a button's ID to everything related
 * to that button, wrapped into [BottomBarButtonInfo] instance
 */
val bottomBarButtonsInfo = hashMapOf(
    R.id.list_tab_button to BottomBarButtonInfo(
        type = FragmentVariant.List,
        outlinedDrawableId = R.drawable.list,
        filledDrawableId = R.drawable.list_filled,
    ),
    R.id.write_tab_button to BottomBarButtonInfo(
        type = FragmentVariant.Write,
        outlinedDrawableId = R.drawable.write,
        filledDrawableId = R.drawable.write_filled,
    ),
    R.id.settings_tab_button to BottomBarButtonInfo(
        type = FragmentVariant.Settings,
        outlinedDrawableId = R.drawable.settings,
        filledDrawableId = R.drawable.settings_filled,
    ),
)
