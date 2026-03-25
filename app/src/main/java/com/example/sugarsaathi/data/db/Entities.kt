package com.example.sugarsaathi.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_ins")
data class CheckIn(
    @PrimaryKey val date: String,          // "YYYY-MM-DD"
    val followedDiet: Boolean,
    val notes: String = "",
    val checkedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "meal_logs")
data class MealLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,                      // "YYYY-MM-DD"
    val mealType: String,                  // "breakfast","lunch","snack","dinner"
    val foodItems: String,                 // comma-separated
    val isHealthy: Boolean,
    val loggedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "blood_sugar_logs")
data class BloodSugarLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,                      // "YYYY-MM-DD"
    val value: Int,                        // mg/dL
    val readingType: String,               // "fasting","post_meal","random"
    val loggedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "hba1c_logs")
data class HbA1cLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,                      // "YYYY-MM-DD"
    val value: Float,                      // percentage
    val loggedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "doctor_visits")
data class DoctorVisit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,                      // "YYYY-MM-DD"
    val doctorName: String,
    val notes: String = "",
    val nextVisitDate: String = ""         // "YYYY-MM-DD" or empty
)