package com.example.sugarsaathi.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    val FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun today(): String = LocalDate.now().format(FORMAT)

    fun LocalDate.fmt() = format(FORMAT)

    fun String.toLocalDate(): LocalDate = LocalDate.parse(this, FORMAT)

    /** Returns blood sugar level label and color resource name */
    fun bloodSugarLabel(value: Int, isFasting: Boolean): Pair<String, String> {
        return if (isFasting) {
            when {
                value < 70  -> Pair("Low", "color_danger")
                value <= 100 -> Pair("Normal", "color_good")
                value <= 125 -> Pair("Pre-diabetic", "color_warning")
                else        -> Pair("High", "color_danger")
            }
        } else {
            when {
                value < 70  -> Pair("Low", "color_danger")
                value <= 140 -> Pair("Normal", "color_good")
                value <= 199 -> Pair("Pre-diabetic", "color_warning")
                else        -> Pair("High", "color_danger")
            }
        }
    }

    fun hba1cLabel(value: Float): Pair<String, String> {
        return when {
            value < 5.7f -> Pair("Normal", "color_good")
            value < 6.5f -> Pair("Pre-diabetic", "color_warning")
            else         -> Pair("Diabetic", "color_danger")
        }
    }
}