package com.example.sugarsaathi

import android.app.Application
import androidx.work.*
import com.example.sugarsaathi.notification.NotificationHelper
import com.example.sugarsaathi.worker.MissedCheckinWorker
import java.util.concurrent.TimeUnit
import java.util.Calendar

class SugarSaathiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannels(this)
        scheduleMissedCheckinWorker()
    }

    private fun scheduleMissedCheckinWorker() {
        // Runs daily at 10 PM to check if user hasn't checked in
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        val delay = target.timeInMillis - now.timeInMillis

        val request = PeriodicWorkRequestBuilder<MissedCheckinWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "missed_checkin",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}