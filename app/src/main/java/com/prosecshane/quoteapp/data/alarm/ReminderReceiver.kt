package com.prosecshane.quoteapp.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prosecshane.quoteapp.data.sharedpreferences.SPConstants
import com.prosecshane.quoteapp.domain.common.toInt
import com.prosecshane.quoteapp.domain.common.toMillis
import com.prosecshane.quoteapp.domain.common.toNotificationPeriod
import com.prosecshane.quoteapp.domain.model.NotificationPeriod
import com.prosecshane.quoteapp.domain.model.Reminder
import com.prosecshane.quoteapp.domain.sharedpreferences.SPApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * [BroadcastReceiver] that triggers at certain times through AlarmManager.
 */
@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {
    /**
     * DaggerHilt injected spApi to read and re-write the reminder data.
     */
    @Inject
    lateinit var spApi: SPApi

    /**
     * Constant containing the notification title.
     */
    private val notificationTitle = "Quotes are waiting!"

    /**
     * Function that converts [NotificationPeriod] to String.
     *
     * @param period [NotificationPeriod] instance to convert.
     */
    private fun periodToString(period: NotificationPeriod): String = when (period) {
        NotificationPeriod.Off ->
            throw Exception("Trying to notify the user when notifications are off")
        NotificationPeriod.Daily -> "day"
        NotificationPeriod.Weekly -> "week"
        NotificationPeriod.Monthly -> "month"
    }

    /**
     * Function that formats the notification description with a period string.
     *
     * @param period [NotificationPeriod] instance to convert to string and use in formatting.
     */
    private fun notificationDescription(period: NotificationPeriod): String =
        "You haven't used the Quote App for over a %s! Click this to come back."
            .format(periodToString(period))

    /**
     * Workaround function! Should not be used!
     * Function that reschedules the reminder.
     *
     * @param context [Context] for creating a [ReminderSchedulerImpl] instance.
     * @param period [NotificationPeriod] instance
     * used to determine when to schedule the next reminder.
     */
    private fun reschedule(context: Context, period: NotificationPeriod) {
        val periodMillis = period.toMillis()
        val firstTime = System.currentTimeMillis() + periodMillis

        runBlocking(Dispatchers.IO) { spApi.set(SPConstants.firstTimeReminderKey, firstTime) }

        val reminderScheduler = ReminderSchedulerImpl(context)
        val reminder = Reminder(period, firstTime)

        reminderScheduler.schedule(reminder)
    }

    /**
     * A trigger function that fires when AlarmManager tells it to do so.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        val period = intent.getIntExtra(
            ReminderConstants.intentKey,
            NotificationPeriod.Off.toInt()
        ).toNotificationPeriod()

        ReminderNotification(context).showNotification(
            notificationTitle,
            notificationDescription(period),
        )

        reschedule(context, period)
    }
}
