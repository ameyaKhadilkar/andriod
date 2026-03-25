package com.example.sugarsaathi.util

import android.content.Context

object Prefs {
    private const val FILE = "sugar_prefs"

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    // Onboarding
    var userName: String
        get() = throw UnsupportedOperationException("use get(ctx)")
        set(_) = throw UnsupportedOperationException("use set(ctx, v)")

    fun getUserName(ctx: Context) = prefs(ctx).getString("user_name", "") ?: ""
    fun setUserName(ctx: Context, v: String) = prefs(ctx).edit().putString("user_name", v).apply()

    fun isOnboardingDone(ctx: Context) = prefs(ctx).getBoolean("onboarding_done", false)
    fun setOnboardingDone(ctx: Context) = prefs(ctx).edit().putBoolean("onboarding_done", true).apply()

    fun getLanguage(ctx: Context) = prefs(ctx).getString("language", "en") ?: "en"
    fun setLanguage(ctx: Context, lang: String) = prefs(ctx).edit().putString("language", lang).apply()

    fun getNotificationHour(ctx: Context) = prefs(ctx).getInt("notif_hour", 21)
    fun getNotificationMinute(ctx: Context) = prefs(ctx).getInt("notif_minute", 0)
    fun setNotificationTime(ctx: Context, hour: Int, minute: Int) {
        prefs(ctx).edit().putInt("notif_hour", hour).putInt("notif_minute", minute).apply()
    }

    fun getStreak(ctx: Context) = prefs(ctx).getInt("streak", 0)
    fun setStreak(ctx: Context, v: Int) = prefs(ctx).edit().putInt("streak", v).apply()

    fun getLastCheckinDate(ctx: Context) = prefs(ctx).getString("last_checkin_date", "") ?: ""
    fun setLastCheckinDate(ctx: Context, date: String) = prefs(ctx).edit().putString("last_checkin_date", date).apply()

    fun isFestivalModeEnabled(ctx: Context) = prefs(ctx).getBoolean("festival_mode", true)
    fun setFestivalMode(ctx: Context, enabled: Boolean) = prefs(ctx).edit().putBoolean("festival_mode", enabled).apply()

    fun getPhotoUri(ctx: Context) = prefs(ctx).getString("family_photo_uri", "") ?: ""
    fun setPhotoUri(ctx: Context, uri: String) = prefs(ctx).edit().putString("family_photo_uri", uri).apply()
}