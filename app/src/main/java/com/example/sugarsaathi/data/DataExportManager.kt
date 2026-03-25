package com.example.sugarsaathi.data

import android.content.Context
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.db.BloodSugarLog
import com.example.sugarsaathi.data.db.CheckIn
import com.example.sugarsaathi.data.db.DoctorVisit
import com.example.sugarsaathi.data.db.HbA1cLog
import com.example.sugarsaathi.data.db.MealLog
import com.example.sugarsaathi.util.Prefs
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

class DataExportManager(private val context: Context) {

    private val db = AppDatabase.get(context)

    // ── Export ────────────────────────────────────────────────────────────────

    suspend fun toJson(): String {
        // Load all data from Room
        val checkIns     = loadAllCheckIns()
        val mealLogs     = loadAllMealLogs()
        val bsLogs       = loadAllBloodSugar()
        val hba1cLogs    = loadAllHba1c()
        val doctorVisits = loadAllDoctorVisits()

        val root = JSONObject().apply {
            put("version", 2)
            put("exportDate", LocalDate.now().toString())
            put("appName", "SugarSaathi")

            // Prefs
            put("prefs", JSONObject().apply {
                put("userName", Prefs.getUserName(context))
                put("language", Prefs.getLanguage(context))
                put("streak", Prefs.getStreak(context))
                put("lastCheckinDate", Prefs.getLastCheckinDate(context))
                put("notifHour", Prefs.getNotificationHour(context))
                put("notifMinute", Prefs.getNotificationMinute(context))
                put("festivalMode", Prefs.isFestivalModeEnabled(context))
            })

            // Check-ins
            put("checkIns", JSONArray().also { arr ->
                checkIns.forEach { entry ->
                    arr.put(JSONObject().apply {
                        put("date", entry.date)
                        put("followedDiet", entry.followedDiet)
                        put("notes", entry.notes)
                        put("checkedAt", entry.checkedAt)
                    })
                }
            })

            // Meal logs
            put("mealLogs", JSONArray().also { arr ->
                mealLogs.forEach { log ->
                    arr.put(JSONObject().apply {
                        put("date", log.date)
                        put("mealType", log.mealType)
                        put("foodItems", log.foodItems)
                        put("isHealthy", log.isHealthy)
                        put("loggedAt", log.loggedAt)
                    })
                }
            })

            // Blood sugar
            put("bloodSugarLogs", JSONArray().also { arr ->
                bsLogs.forEach { log ->
                    arr.put(JSONObject().apply {
                        put("date", log.date)
                        put("value", log.value)
                        put("readingType", log.readingType)
                        put("loggedAt", log.loggedAt)
                    })
                }
            })

            // HbA1c
            put("hba1cLogs", JSONArray().also { arr ->
                hba1cLogs.forEach { log ->
                    arr.put(JSONObject().apply {
                        put("date", log.date)
                        put("value", log.value)
                        put("loggedAt", log.loggedAt)
                    })
                }
            })

            // Doctor visits
            put("doctorVisits", JSONArray().also { arr ->
                doctorVisits.forEach { visit ->
                    arr.put(JSONObject().apply {
                        put("date", visit.date)
                        put("doctorName", visit.doctorName)
                        put("notes", visit.notes)
                        put("nextVisitDate", visit.nextVisitDate)
                    })
                }
            })
        }

        return root.toString(2)
    }

    // ── Import ────────────────────────────────────────────────────────────────

    suspend fun fromJson(json: String): ImportResult {
        try {
            val root = JSONObject(json)

            if (!root.has("appName") || root.getString("appName") != "SugarSaathi") {
                return ImportResult.Error("Not a valid SugarSaathi backup file.")
            }

            val prefs = root.optJSONObject("prefs")
            if (prefs != null) {
                if (prefs.has("userName"))        Prefs.setUserName(context, prefs.getString("userName"))
                if (prefs.has("language"))        Prefs.setLanguage(context, prefs.getString("language"))
                if (prefs.has("streak"))          Prefs.setStreak(context, prefs.getInt("streak"))
                if (prefs.has("lastCheckinDate")) Prefs.setLastCheckinDate(context, prefs.getString("lastCheckinDate"))
                if (prefs.has("notifHour") && prefs.has("notifMinute"))
                    Prefs.setNotificationTime(context, prefs.getInt("notifHour"), prefs.getInt("notifMinute"))
                if (prefs.has("festivalMode"))    Prefs.setFestivalMode(context, prefs.getBoolean("festivalMode"))
            }

            root.optJSONArray("checkIns")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    db.checkInDao().insert(CheckIn(date = obj.getString("date"), followedDiet = obj.getBoolean("followedDiet"), notes = obj.optString("notes", ""), checkedAt = obj.optLong("checkedAt", System.currentTimeMillis())))
                }
            }

            root.optJSONArray("mealLogs")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    db.mealLogDao().insert(MealLog(date = obj.getString("date"), mealType = obj.getString("mealType"), foodItems = obj.getString("foodItems"), isHealthy = obj.optBoolean("isHealthy", true), loggedAt = obj.optLong("loggedAt", System.currentTimeMillis())))
                }
            }

            root.optJSONArray("bloodSugarLogs")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    db.bloodSugarDao().insert(BloodSugarLog(date = obj.getString("date"), value = obj.getInt("value"), readingType = obj.optString("readingType", "random"), loggedAt = obj.optLong("loggedAt", System.currentTimeMillis())))
                }
            }

            root.optJSONArray("hba1cLogs")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    db.hbA1cDao().insert(HbA1cLog(date = obj.getString("date"), value = obj.getDouble("value").toFloat(), loggedAt = obj.optLong("loggedAt", System.currentTimeMillis())))
                }
            }

            root.optJSONArray("doctorVisits")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    db.doctorVisitDao().insert(DoctorVisit(date = obj.getString("date"), doctorName = obj.getString("doctorName"), notes = obj.optString("notes", ""), nextVisitDate = obj.optString("nextVisitDate", "")))
                }
            }

            return ImportResult.Success(root.optString("exportDate", "unknown date"))
        } catch (e: Exception) {
            return ImportResult.Error(e.message ?: "Unknown error")
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private suspend fun loadAllCheckIns(): List<CheckIn> {
        return db.checkInDao().getRange("2000-01-01", "2999-12-31")
    }

    private suspend fun loadAllMealLogs(): List<MealLog> {
        return db.mealLogDao().getRange("2000-01-01", "2999-12-31")
    }

    private suspend fun loadAllBloodSugar(): List<BloodSugarLog> {
        return db.bloodSugarDao().getRange("2000-01-01", "2999-12-31")
    }

    private suspend fun loadAllHba1c(): List<HbA1cLog> {
        return db.hbA1cDao().getAllSuspend()
    }

    private suspend fun loadAllDoctorVisits(): List<DoctorVisit> {
        return db.doctorVisitDao().getAllSuspend()
    }

    sealed class ImportResult {
        data class Success(val exportDate: String) : ImportResult()
        data class Error(val message: String) : ImportResult()
    }
}