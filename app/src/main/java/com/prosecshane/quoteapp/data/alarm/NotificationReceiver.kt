package com.prosecshane.quoteapp.data.alarm

import android.app.NotificationManager
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
 * [BroadcastReceiver] that catches actions from the notification.
 */
@AndroidEntryPoint
class NotificationReceiver: BroadcastReceiver() {
    /**
     * DaggerHilt injected spApi to read and re-write the reminder data.
     */
    @Inject
    lateinit var spApi: SPApi

    /**
     * A trigger function that fires on notification action.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)

        val reminderScheduler = ReminderSchedulerImpl(context)
        val reminder = runBlocking(Dispatchers.IO) {
            val period = spApi.get(
                SPConstants.notificationPeriodKey,
                NotificationPeriod.Off.toInt(),
            ).toNotificationPeriod()
            val firstTime = spApi.get(SPConstants.firstTimeReminderKey, 0L)

            Reminder(period, firstTime)
        }
        reminderScheduler.cancel(reminder)

        runBlocking(Dispatchers.IO) {
            spApi.set(SPConstants.notificationPeriodKey, NotificationPeriod.Off.toInt())
        }
    }
}
