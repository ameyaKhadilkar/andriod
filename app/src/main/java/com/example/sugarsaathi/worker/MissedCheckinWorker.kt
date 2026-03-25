package com.example.sugarsaathi.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sugarsaathi.notification.NotificationHelper
import com.example.sugarsaathi.util.DateUtils
import com.example.sugarsaathi.util.Prefs

class MissedCheckinWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val lastDate = Prefs.getLastCheckinDate(applicationContext)
        if (lastDate != DateUtils.today()) {
            NotificationHelper.showMissedCheckinNotification(applicationContext)
        }
        return Result.success()
    }
}