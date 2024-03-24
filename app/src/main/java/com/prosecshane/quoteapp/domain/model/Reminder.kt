package com.prosecshane.quoteapp.domain.model

import com.prosecshane.quoteapp.domain.common.toMillis

/**
 * Data class that contains information about reminding the user to use this app.
 *
 * @param period [NotificationPeriod] instance that shows how often the notification should appear.
 * @param firstTime Millis timestamp of the first reminder.
 */
data class Reminder(
    val period: NotificationPeriod,
    val firstTime: Long = System.currentTimeMillis() + period.toMillis(),
)
