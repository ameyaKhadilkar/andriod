package com.example.sugarsaathi.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CheckInDao {
    @Query("SELECT * FROM check_ins ORDER BY date DESC")
    fun getAll(): LiveData<List<CheckIn>>

    @Query("SELECT * FROM check_ins WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): CheckIn?

    @Query("SELECT * FROM check_ins WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    suspend fun getRange(start: String, end: String): List<CheckIn>

    @Query("SELECT * FROM check_ins WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    fun getRangeLive(start: String, end: String): LiveData<List<CheckIn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkIn: CheckIn)

    @Delete
    suspend fun delete(checkIn: CheckIn)
}

@Dao
interface MealLogDao {
    @Query("SELECT * FROM meal_logs WHERE date = :date ORDER BY loggedAt ASC")
    fun getByDate(date: String): LiveData<List<MealLog>>

    @Query("SELECT * FROM meal_logs WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    suspend fun getRange(start: String, end: String): List<MealLog>

    @Insert
    suspend fun insert(log: MealLog)

    @Delete
    suspend fun delete(log: MealLog)
}

@Dao
interface BloodSugarDao {
    @Query("SELECT * FROM blood_sugar_logs ORDER BY date DESC, loggedAt DESC")
    fun getAll(): LiveData<List<BloodSugarLog>>

    @Query("SELECT * FROM blood_sugar_logs WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    suspend fun getRange(start: String, end: String): List<BloodSugarLog>

    @Insert
    suspend fun insert(log: BloodSugarLog)

    @Delete
    suspend fun delete(log: BloodSugarLog)
}

@Dao
interface HbA1cDao {
    @Query("SELECT * FROM hba1c_logs ORDER BY date DESC")
    fun getAll(): LiveData<List<HbA1cLog>>

    @Query("SELECT * FROM hba1c_logs ORDER BY date DESC")
    suspend fun getAllSuspend(): List<HbA1cLog>

    @Insert
    suspend fun insert(log: HbA1cLog)

    @Delete
    suspend fun delete(log: HbA1cLog)
}

@Dao
interface DoctorVisitDao {
    @Query("SELECT * FROM doctor_visits ORDER BY date DESC")
    fun getAll(): LiveData<List<DoctorVisit>>

    @Query("SELECT * FROM doctor_visits ORDER BY date DESC")
    suspend fun getAllSuspend(): List<DoctorVisit>

    @Insert
    suspend fun insert(visit: DoctorVisit)

    @Update
    suspend fun update(visit: DoctorVisit)

    @Delete
    suspend fun delete(visit: DoctorVisit)
}