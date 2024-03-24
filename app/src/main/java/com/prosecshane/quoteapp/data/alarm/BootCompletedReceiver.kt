package com.prosecshane.quoteapp.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prosecshane.quoteapp.data.sharedpreferences.SPConstants
import com.prosecshane.quoteapp.domain.common.toInt
import com.prosecshane.quoteapp.domain.common.toNotificationPeriod
import com.prosecshane.quoteapp.domain.model.NotificationPeriod
import com.prosecshane.quoteapp.domain.model.Reminder
import com.prosecshane.quoteapp.domain.sharedpreferences.SPApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * [BroadcastReceiver] that reschedules the reminder on reboot.
 */
@AndroidEntryPoint
class BootCompletedReceiver: BroadcastReceiver() {
    /**
     * DaggerHilt injected spApi to read the reminder data.
     */
    @Inject
    lateinit var spApi: SPApi

    /**
     * A trigger function that fires on reboot.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null || intent.action != Intent.ACTION_BOOT_COMPLETED)
            return

        val reminderScheduler = ReminderSchedulerImpl(context)
        val reminder = runBlocking(Dispatchers.IO) {
            val period = spApi.get(
                SPConstants.notificationPeriodKey,
                NotificationPeriod.Off.toInt()
            ).toNotificationPeriod()
            val firstTime = spApi.get(SPConstants.firstTimeReminderKey, 0L)
            Reminder(period = period, firstTime = firstTime)
        }
        reminderScheduler.schedule(reminder)
    }
}
