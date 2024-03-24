package com.prosecshane.quoteapp.data.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.prosecshane.quoteapp.R
import com.prosecshane.quoteapp.presentation.ui.activity.MainActivity

/**
 * Class that is used to create reminding notifications.
 *
 * @param context [Context] in which to create notifications.
 */
class ReminderNotification(private val context: Context) {
    /**
     * Notification Manager that will post notifications.
     */
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * Show a reminding notification.
     *
     * @param title Title of the notification.
     * @param description Description of the notification.
     */
    fun showNotification(title: String, description: String) {
        val pendingActivityIntent = PendingIntent.getActivity(
            context,
            1,
            Intent(context, MainActivity::class.java).apply {
                putExtra("from", "notification")
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        val pendingTurnOffIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, ReminderConstants.channelId)
            .setSmallIcon(R.drawable.notif_cropped)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingActivityIntent)
            .addAction(
                R.drawable.notif_cropped,
                ReminderConstants.turnOffActionText,
                pendingTurnOffIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }
}
