package my.vladpustovalov.thedisciplineprogram.domain.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import my.vladpustovalov.thedisciplineprogram.R

class PaymentReminderReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
        
        val notificationManager = LocalNotificationManager(context)
        notificationManager.scheduleMonthlyPaymentReminder()
    }

    private fun showNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "payment_reminder_channel"

        val channel = NotificationChannel(
            channelId,
            context.getString(R.string.notification_channel_payment_reminders),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(context.getString(R.string.notification_payment_reminder_title))
            .setContentText(context.getString(R.string.notification_payment_reminder_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        try {
            manager.notify(1001, notification)
        } catch (e: SecurityException) {
            // Permission for POST_NOTIFICATIONS is missing.
            // Ensure permission is requested at UI level before scheduling.
        }
    }
}
