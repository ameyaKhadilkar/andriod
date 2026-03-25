package com.example.sugarsaathi.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.sugarsaathi.R
import com.example.sugarsaathi.ui.home.HomeActivity
import com.example.sugarsaathi.util.Prefs
import java.util.Calendar

object NotificationHelper {

    const val CHANNEL_ID = "checkin_channel"
    const val CHANNEL_REMINDER_ID = "reminder_channel"
    const val NOTIF_ID_CHECKIN = 1001
    const val NOTIF_ID_MISSED = 1002

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, context.getString(R.string.notif_channel_name), NotificationManager.IMPORTANCE_HIGH).apply {
                    description = context.getString(R.string.notif_channel_desc)
                }
            )
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL_REMINDER_ID, context.getString(R.string.notif_channel_reminder_name), NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
    }

    fun scheduleNightlyCheckin(context: Context) {
        val hour = Prefs.getNotificationHour(context)
        val minute = Prefs.getNotificationMinute(context)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = PendingIntent.getBroadcast(
            context, 0,
            Intent(context, CheckinReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, intent)
    }

    fun showCheckinNotification(context: Context) {
        val userName = Prefs.getUserName(context)
        val isHindi = Prefs.getLanguage(context) == "hi"

        val title = if (isHindi) "नमस्ते $userName! 🌙" else "Good evening, $userName! 🌙"
        val body = if (isHindi)
            "आज का डाइट चेक-इन का समय हो गया। आपने आज कैसा खाया?"
        else
            "Time for your daily diet check-in. How did you eat today?"

        val tapIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("open_checkin", true)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(tapIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val nm = context.getSystemService(NotificationManager::class.java)
        nm.notify(NOTIF_ID_CHECKIN, notification)
    }

    fun showMissedCheckinNotification(context: Context) {
        val isHindi = Prefs.getLanguage(context) == "hi"
        val title = if (isHindi) "याद दिलाना — चेक-इन बाकी है!" else "Reminder — Check-in pending!"
        val body = if (isHindi)
            "आपने आज अभी तक चेक-इन नहीं किया। अभी करें!"
        else
            "You have not checked in today yet. Do it now!"

        val tapIntent = PendingIntent.getActivity(
            context, 1,
            Intent(context, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("open_checkin", true)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDER_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(tapIntent)
            .setAutoCancel(true)
            .build()

        val nm = context.getSystemService(NotificationManager::class.java)
        nm.notify(NOTIF_ID_MISSED, notification)
    }
}