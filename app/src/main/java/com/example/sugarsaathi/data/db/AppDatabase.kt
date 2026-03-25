package com.example.sugarsaathi.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CheckIn::class, MealLog::class, BloodSugarLog::class, HbA1cLog::class, DoctorVisit::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun checkInDao(): CheckInDao
    abstract fun mealLogDao(): MealLogDao
    abstract fun bloodSugarDao(): BloodSugarDao
    abstract fun hbA1cDao(): HbA1cDao
    abstract fun doctorVisitDao(): DoctorVisitDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "sugarsaathi.db")
                .build().also { INSTANCE = it }
        }
    }
}