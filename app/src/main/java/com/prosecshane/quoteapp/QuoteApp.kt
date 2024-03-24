package com.prosecshane.quoteapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.prosecshane.quoteapp.data.alarm.ReminderConstants
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application context, used with DaggerHilt.
 */
@HiltAndroidApp
class QuoteApp: Application() {
    /**
     * Create notification channel on app create.
     */
    override fun onCreate() {
        super.onCreate()
        createReminderNotificationChannel()
    }

    /**
     * Create notification channel for reminders.
     */
    private fun createReminderNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ReminderConstants.channelId,
                ReminderConstants.channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = ReminderConstants.channelDescription
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
