package com.prosecshane.quoteapp.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.prosecshane.quoteapp.domain.alarm.ReminderScheduler
import com.prosecshane.quoteapp.domain.common.toInt
import com.prosecshane.quoteapp.domain.model.NotificationPeriod
import com.prosecshane.quoteapp.domain.model.Reminder

/**
 * Implementation of the interface [ReminderScheduler].
 * Schedules and cancels reminders.
 *
 * @param context [Context] to work in.
 */
class ReminderSchedulerImpl(
    private val context: Context
) : ReminderScheduler {
    /**
     * Alarm Manager that will schedule and cancel reminders.
     */
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    /**
     * Function uses a workaround with setExact!
     * Function that schedules a reminder.
     *
     * @param reminder [Reminder] instance containing information for the reminder to schedule.
     */
    override fun schedule(reminder: Reminder) {
        if (reminder.period == NotificationPeriod.Off) return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderConstants.intentKey, reminder.period.toInt())
        }

        alarmManager.setExact(
            AlarmManager.RTC,
            reminder.firstTime,
            PendingIntent.getBroadcast(
                context,
                100,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    /**
     * Function that cancels a reminder.
     *
     * @param reminder [Reminder] instance containing information for the reminder to cancel.
     */
    override fun cancel(reminder: Reminder) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                100,
                Intent(context, ReminderReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
