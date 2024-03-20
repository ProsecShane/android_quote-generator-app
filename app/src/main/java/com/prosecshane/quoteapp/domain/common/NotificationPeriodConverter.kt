package com.prosecshane.quoteapp.domain.common

import com.prosecshane.quoteapp.domain.model.NotificationPeriod

/**
 * List of all possible instances of the [NotificationPeriod] class in a certain order.
 */
val allPeriods = listOf(
    NotificationPeriod.Off,
    NotificationPeriod.Daily,
    NotificationPeriod.Weekly,
    NotificationPeriod.Monthly,
)

/**
 * Extension function for [NotificationPeriod] that converts an instance into a number.
 *
 * @return The number associated with the given instance.
 */
fun NotificationPeriod.toInt(): Int = allPeriods.indexOf(this)

/**
 * Extenstion function for [NotificationPeriod] that converts an instance into milliseconds.
 *
 * @return The number of milliseconds representing a given period.
 */
fun NotificationPeriod.toMillis(): Long = when (this) {
    NotificationPeriod.Off -> -1
    NotificationPeriod.Daily -> 86400000
    NotificationPeriod.Weekly -> 604800000
    NotificationPeriod.Monthly -> 2592000000
}

/**
 * Extension function for [Int] that converts a number into a [NotificationPeriod] instance.
 *
 * @return A [NotificationPeriod] instance associated with the given number.
 */
fun Int.toNotificationPeriod(): NotificationPeriod {
    return allPeriods.getOrElse(this) { NotificationPeriod.Off }
}
