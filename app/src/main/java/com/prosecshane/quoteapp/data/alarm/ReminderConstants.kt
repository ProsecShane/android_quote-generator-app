package com.prosecshane.quoteapp.data.alarm

/**
 * Singleton constants object containing information about reminding the user to use the app.
 */
object ReminderConstants {
    const val intentKey = "period"

    const val channelId = "reminderChannel"
    const val channelName = "Reminder"
    const val channelDescription = "Periodically reminds the user to use this app"
    const val turnOffActionText = "Turn off notifications"
}
