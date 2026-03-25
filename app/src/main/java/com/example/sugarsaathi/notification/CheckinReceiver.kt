package com.example.sugarsaathi.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.sugarsaathi.util.DateUtils
import com.example.sugarsaathi.util.Prefs

class CheckinReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val lastDate = Prefs.getLastCheckinDate(context)
        if (lastDate == DateUtils.today()) return   // already checked in
        NotificationHelper.showCheckinNotification(context)
    }
}