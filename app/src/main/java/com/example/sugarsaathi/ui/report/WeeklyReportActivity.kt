package com.example.sugarsaathi.ui.report

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.repository.CheckInRepository
import com.example.sugarsaathi.databinding.ActivityWeeklyReportBinding
import com.example.sugarsaathi.util.DateUtils.fmt
import com.example.sugarsaathi.util.Prefs
import kotlinx.coroutines.launch
import java.time.LocalDate

class WeeklyReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeeklyReportBinding
    private lateinit var repo: CheckInRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeeklyReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.report_title)
        toolbar.setNavigationOnClickListener { finish() }

        repo = CheckInRepository(AppDatabase.get(this), this)
        loadReport()
    }

    private fun loadReport() {
        val today = LocalDate.now()
        val startDate = today.minusDays(6)
        val isHindi = Prefs.getLanguage(this) == "hi"

        lifecycleScope.launch {
            val entries = repo.getRange(startDate.fmt(), today.fmt())
            val goodDays = entries.count { it.followedDiet }
            val badEntries = entries.filter { !it.followedDiet && it.notes.isNotEmpty() }

            binding.tvDaysCount.text = getString(R.string.report_days_count, goodDays)

            binding.tvSummary.text = when {
                goodDays == 7 -> getString(R.string.report_msg_perfect)
                goodDays >= 5 -> getString(R.string.report_msg_excellent)
                goodDays >= 3 -> getString(R.string.report_msg_good)
                goodDays >= 1 -> resources.getQuantityString(R.plurals.report_msg_some, goodDays, goodDays)
                else          -> getString(R.string.report_msg_tough)
            }

            val days = (0..6).map { today.minusDays((6 - it).toLong()) }
            val dayViews = listOf(binding.day1, binding.day2, binding.day3, binding.day4, binding.day5, binding.day6, binding.day7)
            days.forEachIndexed { i, date ->
                val entry = entries.find { it.date == date.fmt() }
                dayViews[i].text = date.dayOfWeek.name.take(3)
                dayViews[i].setBackgroundResource(when {
                    entry == null      -> R.drawable.bg_day_neutral
                    entry.followedDiet -> R.drawable.bg_day_good
                    else               -> R.drawable.bg_day_bad
                })
            }

            if (badEntries.isEmpty()) {
                binding.cardNotes.visibility = View.GONE
            } else {
                binding.cardNotes.visibility = View.VISIBLE
                binding.tvNotes.text = badEntries.joinToString("\n\n") { "📅 ${it.date}\n${it.notes}" }
            }

            binding.btnShare.setOnClickListener {
                val userName = Prefs.getUserName(this@WeeklyReportActivity)
                val text = if (isHindi)
                    "नमस्ते! मैं $userName हूँ। इस सप्ताह मेरे $goodDays/7 दिन अच्छे रहे! 💪 #SugarSaathi"
                else
                    "Hi! I'm $userName. This week I had $goodDays/7 good diet days! 💪 #SugarSaathi"
                val share = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text); setPackage("com.whatsapp") }
                try { startActivity(share) } catch (_: Exception) { startActivity(Intent.createChooser(share.apply { setPackage(null) }, getString(R.string.share_via))) }
            }
        }
    }
}