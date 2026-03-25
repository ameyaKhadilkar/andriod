package com.example.sugarsaathi.data.repository

import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.db.CheckIn
import android.content.Context
import com.example.sugarsaathi.util.DateUtils
import com.example.sugarsaathi.util.DateUtils.fmt
import com.example.sugarsaathi.util.DateUtils.toLocalDate
import com.example.sugarsaathi.util.Prefs
import java.time.LocalDate

class CheckInRepository(private val db: AppDatabase, private val ctx: Context) {

    val allCheckIns = db.checkInDao().getAll()

    suspend fun checkinToday(followedDiet: Boolean, notes: String = "") {
        val today = DateUtils.today()
        db.checkInDao().insert(CheckIn(today, followedDiet, notes))
        Prefs.setLastCheckinDate(ctx, today)
        recomputeStreak()
    }

    suspend fun editEntry(date: LocalDate, followedDiet: Boolean, notes: String) {
        db.checkInDao().insert(CheckIn(date.fmt(), followedDiet, notes))
        recomputeStreak()
    }

    suspend fun getByDate(date: String) = db.checkInDao().getByDate(date)

    suspend fun getRange(start: String, end: String) = db.checkInDao().getRange(start, end)

    fun getRangeLive(start: String, end: String) = db.checkInDao().getRangeLive(start, end)

    private suspend fun recomputeStreak() {
        var count = 0
        var date = LocalDate.now()
        while (true) {
            val entry = db.checkInDao().getByDate(date.fmt())
            if (entry != null && entry.followedDiet) {
                count++
                date = date.minusDays(1)
            } else break
        }
        Prefs.setStreak(ctx, count)
    }

    fun getStreak() = Prefs.getStreak(ctx)
}