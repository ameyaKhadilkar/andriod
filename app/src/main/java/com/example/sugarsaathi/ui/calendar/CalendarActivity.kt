package com.example.sugarsaathi.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.db.CheckIn
import com.example.sugarsaathi.data.repository.CheckInRepository
import com.example.sugarsaathi.databinding.ActivityCalendarBinding
import com.example.sugarsaathi.databinding.DialogEditEntryBinding
import com.example.sugarsaathi.util.DateUtils.fmt
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var repo: CheckInRepository
    private var currentMonth = YearMonth.now()
    private var entries: Map<String, CheckIn> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.calendar_title)
        toolbar.setNavigationOnClickListener { finish() }

        repo = CheckInRepository(AppDatabase.get(this), this)

        binding.btnPrevMonth.setOnClickListener { currentMonth = currentMonth.minusMonths(1); loadCalendar() }
        binding.btnNextMonth.setOnClickListener { currentMonth = currentMonth.plusMonths(1); loadCalendar() }

        loadCalendar()
    }

    private fun loadCalendar() {
        val start = currentMonth.atDay(1).fmt()
        val end = currentMonth.atEndOfMonth().fmt()
        binding.tvMonthYear.text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}"
        lifecycleScope.launch {
            entries = repo.getRange(start, end).associateBy { it.date }
            renderGrid()
        }
    }

    private fun renderGrid() {
        binding.calendarGrid.removeAllViews()
        val today = LocalDate.now()
        val firstDay = currentMonth.atDay(1)
        val daysInMonth = currentMonth.lengthOfMonth()
        val startDow = firstDay.dayOfWeek.value % 7

        listOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat").forEach { h ->
            val tv = layoutInflater.inflate(R.layout.item_cal_header, binding.calendarGrid, false) as android.widget.TextView
            tv.text = h
            binding.calendarGrid.addView(tv)
        }

        repeat(startDow) {
            val empty = View(this)
            empty.layoutParams = GridLayout.LayoutParams().apply {
                width = 0; height = resources.getDimensionPixelSize(R.dimen.cal_cell_height)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            binding.calendarGrid.addView(empty)
        }

        for (day in 1..daysInMonth) {
            val date = currentMonth.atDay(day)
            val entry = entries[date.fmt()]
            val isPast = !date.isAfter(today)
            val cell = layoutInflater.inflate(R.layout.item_cal_day, binding.calendarGrid, false)
            val tv = cell.findViewById<android.widget.TextView>(R.id.tvDay)
            tv.text = day.toString()
            when {
                isPast && entry != null -> {
                    cell.setBackgroundColor(ContextCompat.getColor(this, if (entry.followedDiet) R.color.green_good else R.color.red_danger))
                    tv.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                date == today -> {
                    cell.setBackgroundColor(ContextCompat.getColor(this, R.color.saffron))
                    tv.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
            if (isPast) cell.setOnClickListener { showEditDialog(date, entry) }
            binding.calendarGrid.addView(cell)
        }
    }

    private fun showEditDialog(date: LocalDate, existing: CheckIn?) {
        val dialogBinding = DialogEditEntryBinding.inflate(LayoutInflater.from(this))
        dialogBinding.tvDialogDate.text = date.fmt()
        if (existing != null) {
            if (existing.followedDiet) dialogBinding.rbYes.isChecked = true else dialogBinding.rbNo.isChecked = true
            dialogBinding.etNotes.setText(existing.notes)
        }
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_edit_title))
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_save) { _, _ ->
                lifecycleScope.launch {
                    repo.editEntry(date, dialogBinding.rbYes.isChecked, dialogBinding.etNotes.text.toString().trim())
                    loadCalendar()
                }
            }
            .setNegativeButton(R.string.btn_cancel, null).show()
    }
}