package com.prosecshane.quoteapp.domain.alarm

import com.prosecshane.quoteapp.domain.model.Reminder

/**
 * Interface that needs to be implemented. Describes a class that will schedule reminders.
 */
interface ReminderScheduler {
    /**
     * Function that will schedule a reminder.
     *
     * @param reminder [Reminder] instance containing information for the reminder to schedule.
     */
    fun schedule(reminder: Reminder)

    /**
     * Function that will cancel a reminder.
     *
     * @param reminder [Reminder] instance containing information for the reminder to cancel.
     */
    fun cancel(reminder: Reminder)
}
