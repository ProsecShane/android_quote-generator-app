package com.prosecshane.quoteapp.presentation.ui.fragment

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.domain.common.allPeriods
import com.prosecshane.quoteapp.domain.common.toInt
import com.prosecshane.quoteapp.domain.common.toNotificationPeriod
import com.prosecshane.quoteapp.domain.model.NotificationPeriod
import com.prosecshane.quoteapp.presentation.viewmodel.LocalDataViewModel
import com.prosecshane.quoteapp.utils.Permission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A [Fragment] containing settings for the app.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {
    /**
     * A ViewModel that deals with all the data stored locally.
     * Used to set the settings and clear quotes if requested.
     */
    private val localDataViewModel: LocalDataViewModel by activityViewModels()

    /**
     * Obligatory function to set the layout for the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    /**
     * Function that sets up all the logic in the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swiped: ConstraintLayout = view.findViewById(R.id.settings_swiped)
        val swipedMark: CheckBox = view.findViewById(R.id.settings_swiped_mark)

        val inQuote: ConstraintLayout = view.findViewById(R.id.settings_in_quote)
        val inQuoteMark: CheckBox = view.findViewById(R.id.settings_in_quote_mark)

        val notificationOption: LinearLayout = view.findViewById(R.id.settings_notification)
        val notificationPeriod: TextView = view.findViewById(R.id.settings_notification_period)

        val clearButton: MaterialButton = view.findViewById(R.id.settings_clear)

        bindCheckableOptions(swiped, swipedMark, inQuote, inQuoteMark)
        bindClearButton(clearButton)
        bindNotificationOption(notificationOption)

        lifecycleScope.launch {
            localDataViewModel.askWhenSwiped.collect {
                swipedMark.isChecked = it
            }
        }

        lifecycleScope.launch {
            localDataViewModel.askWhenInQuote.collect {
                inQuoteMark.isChecked = it
            }
        }

        lifecycleScope.launch {
            localDataViewModel.notificationPeriod.collect {
                notificationPeriod.text = getString(
                    R.string.settings_notification_label,
                    getString(when (it) {
                        NotificationPeriod.Off -> R.string.settings_notification_off
                        NotificationPeriod.Daily -> R.string.settings_notification_daily
                        NotificationPeriod.Weekly -> R.string.settings_notification_weekly
                        NotificationPeriod.Monthly -> R.string.settings_notification_monthly
                    } )
                )
            }
        }
    }

    /**
     * Update the notification period value in [LocalDataViewModel] instance
     * and reset it if the permission was revoked on Fragment resume.
     */
    override fun onResume() {
        super.onResume()

        localDataViewModel.updateNotificationPeriod()
        if (!Permission.isGranted(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS))
            localDataViewModel.setNotificationPeriod(NotificationPeriod.Off)
    }

    /**
     * Binds the options with a CheckBox.
     *
     * @param swiped The option that sets whether the user needs to confirm the deletion on swipe.
     * @param swipedMark The [CheckBox] of the option that sets
     * whether the user needs to confirm the deletion on swipe.
     * @param inQuote The option that sets whether the user needs to confirm the deletion on swipe.
     * @param inQuoteMark The [CheckBox] of the option that sets
     * whether the user needs to confirm the deletion on swipe.
     */
    private fun bindCheckableOptions(
        swiped: ConstraintLayout,
        swipedMark: CheckBox,
        inQuote: ConstraintLayout,
        inQuoteMark: CheckBox,
    ) {
        swiped.setOnClickListener {
            localDataViewModel.setAskWhenSwiped(!localDataViewModel.askWhenSwiped.value)
        }
        swipedMark.setOnClickListener {
            localDataViewModel.setAskWhenSwiped(!localDataViewModel.askWhenSwiped.value)
        }
        inQuote.setOnClickListener {
            localDataViewModel.setAskWhenInQuote(!localDataViewModel.askWhenInQuote.value)
        }
        inQuoteMark.setOnClickListener {
            localDataViewModel.setAskWhenInQuote(!localDataViewModel.askWhenInQuote.value)
        }
    }

    /**
     * Binds the reminder notification option.
     *
     * @param option The option layout that will respond to clicks.
     */
    private fun bindNotificationOption(option: LinearLayout) {
        val increment: () -> Unit = {
            val currentValue = localDataViewModel.notificationPeriod.value.toInt()
            val newValue = (currentValue + 1) % allPeriods.size
            localDataViewModel.setNotificationPeriod(newValue.toNotificationPeriod())
        }
        option.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                increment()
            } else {
                val permissionGranted = Permission.isGranted(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS,
                )
                if (!permissionGranted) {
                    askNotificationsPermissionDialog {
                        Permission.request(
                            requireActivity(),
                            android.Manifest.permission.POST_NOTIFICATIONS,
                            10
                        )
                    }
                } else increment()
            }
        }
    }

    /**
     * Binds the Button that clears all the quotes.
     */
    private fun bindClearButton(clearButton: MaterialButton) {
        clearButton.setOnClickListener {
             confirmClear { localDataViewModel.clearQuotes() }
        }
    }

    /**
     * First confirmation of the quotes' clear.
     *
     * @param onDeleteCallback Callback that clears all the quotes.
     */
    private fun confirmClear(
        onDeleteCallback: () -> Unit,
    ) {
        val title = getString(R.string.clear_title)
        val description = getString(R.string.clear_description)
        val delete = getString(R.string.delete_yes)
        val cancel = getString(R.string.delete_no)

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(delete) { _, _ -> confirmClear2(onDeleteCallback) }
            .setNegativeButton(cancel) { _, _ -> }
            .setOnCancelListener { }
            .create()
            .show()
    }

    /**
     * Second confirmation of the quotes' clear.
     *
     * @param onDeleteCallback Callback that clears all the quotes.
     */
    private fun confirmClear2(
        onDeleteCallback: () -> Unit,
    ) {
        val title = getString(R.string.clear_title)
        val description = getString(R.string.clear_description_2)
        val delete = getString(R.string.delete_yes)
        val cancel = getString(R.string.delete_no)

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(delete) { _, _ -> onDeleteCallback() }
            .setNegativeButton(cancel) { _, _ -> }
            .setOnCancelListener { }
            .create()
            .show()
    }

    /**
     * [AlertDialog] that informs the user that the app needs permission to send notifications.
     *
     * @param onReadCallback Callback that gets called after the user read the [AlertDialog].
     */
    private fun askNotificationsPermissionDialog(onReadCallback: () -> Unit) {
        val title = getString(R.string.settings_notification_ask_title)
        val description = getString(R.string.settings_notification_ask_desc)
        val ok = getString(R.string.settings_notification_ask_ok)

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(ok) { _, _ -> onReadCallback()}
            .setOnCancelListener { onReadCallback() }
            .create()
            .show()
    }
}
