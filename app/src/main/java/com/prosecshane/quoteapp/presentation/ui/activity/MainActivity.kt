package com.prosecshane.quoteapp.presentation.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.presentation.ui.common.FragmentVariant
import com.prosecshane.quoteapp.presentation.ui.common.bottomBarButtonsInfo
import com.prosecshane.quoteapp.presentation.viewmodel.QuoteAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * App's main and only activity. Has a fragment container
 * where all the different GUI branches of the app are stored.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /**
     * Navigation controller, used to transfer between fragments.
     */
    private lateinit var navController: NavController

    /**
     * A list of [ImageButton]s that are shown on the bottom of the activity.
     */
    private lateinit var bottomBarButtons: List<ImageButton>

    /**
     * ViewModel for the activity. Contains state holders so that
     * the activity stays consistent even after being recreated.
     */
    private val quoteAppViewModel: QuoteAppViewModel by viewModels()

    /**
     * Callback for the activity on having the back button pressed.
     */
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (quoteAppViewModel.lastPressedStack.value.size > 1) {
                quoteAppViewModel.popLastPressedStack()
            } else finish()
        }
    }

    /**
     * Required [onCreate] function. Sets the back button callback,
     * defines the lateinit variables, connects the ViewModel's
     * data to activity's UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        bottomBarButtons = listOf(
            findViewById(R.id.list_tab_button),
            findViewById(R.id.write_tab_button),
            findViewById(R.id.settings_tab_button),
        )

        bindBottomBarButtons(bottomBarButtons)

        lifecycleScope.launch {
            quoteAppViewModel.lastPressedStack.collect {
                styleBottomBarButtons(bottomBarButtons, it.last())
                gotoFragment(it.last())
            }
        }
    }

    /**
     * Binds the bottom bar buttons to have them switch fragments
     * in the container according to what button was pressed.
     *
     * @param buttons A list of [ImageButton]s to bind.
     */
    private fun bindBottomBarButtons(buttons: List<ImageButton>) {
        for (button in buttons) {
            button.setOnClickListener {
                val info = bottomBarButtonsInfo[button.id]
                    ?: throw Exception("Could not find information to bind bottom bar button")
                if (info.type != quoteAppViewModel.lastPressedStack.value.last()) {
                    quoteAppViewModel.addToLastPressedStack(info.type)
                }
            }
        }
    }

    /**
     * Styles the bottom bar buttons according to
     * what fragment is currently displayed inside the container.
     *
     * @param buttons A list of [ImageButton]s to style.
     * @param lastPressed Information about what fragment is displayed currently.
     */
    private fun styleBottomBarButtons(
        buttons: List<ImageButton>,
        lastPressed: FragmentVariant,
    ) {
        for (button in buttons) {
            val info = bottomBarButtonsInfo[button.id]
                ?: throw Exception("Could not find information to style bottom bar buttons")

            if (info.type != lastPressed) {
                button.setImageResource(info.outlinedDrawableId)
                button.setColorFilter(ContextCompat.getColor(this, R.color.divider))
                button.isEnabled = true
            } else {
                button.setImageResource(info.filledDrawableId)
                button.setColorFilter(ContextCompat.getColor(this, R.color.on_background))
                button.isEnabled = false
            }
        }
    }

    /**
     * Change the fragment inside the fragment container.
     *
     * @param fragmentVariant Specifies what fragment the container should switch to.
     */
    private fun gotoFragment(fragmentVariant: FragmentVariant) {
        navController.navigate(
            when (fragmentVariant) {
                FragmentVariant.List -> R.id.listFragment
                FragmentVariant.Quote -> R.id.quoteFragment
                FragmentVariant.Write -> R.id.writeFragment
                FragmentVariant.Settings -> R.id.settingsFragment
            }
        )
    }
}
