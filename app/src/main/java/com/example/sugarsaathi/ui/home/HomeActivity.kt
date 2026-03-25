package com.example.sugarsaathi.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.repository.CheckInRepository
import com.example.sugarsaathi.databinding.ActivityHomeBinding
import com.example.sugarsaathi.ui.bloodsugar.BloodSugarActivity
import com.example.sugarsaathi.ui.calendar.CalendarActivity
import com.example.sugarsaathi.ui.checkin.CheckinActivity
import com.example.sugarsaathi.ui.doctor.DoctorActivity
import com.example.sugarsaathi.ui.meal.MealActivity
import com.example.sugarsaathi.ui.report.WeeklyReportActivity
import com.example.sugarsaathi.ui.settings.SettingsActivity
import com.example.sugarsaathi.util.DateUtils
import com.example.sugarsaathi.util.FestivalHelper
import com.example.sugarsaathi.util.Prefs
import com.example.sugarsaathi.util.QuoteRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var repo: CheckInRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = CheckInRepository(AppDatabase.get(this), this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "SugarSaathi"

        // Hamburger toggle
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Drawer item clicks
        binding.navigationView.setNavigationItemSelectedListener { item ->
            binding.drawerLayout.closeDrawers()
            when (item.itemId) {
                R.id.nav_meal        -> startActivity(Intent(this, MealActivity::class.java))
                R.id.nav_blood_sugar -> startActivity(Intent(this, BloodSugarActivity::class.java))
                R.id.nav_calendar    -> startActivity(Intent(this, CalendarActivity::class.java))
                R.id.nav_report      -> startActivity(Intent(this, WeeklyReportActivity::class.java))
                R.id.nav_doctor      -> startActivity(Intent(this, DoctorActivity::class.java))
                R.id.nav_settings    -> startActivity(Intent(this, SettingsActivity::class.java))
            }
            true
        }

        setupUI()

        binding.btnCheckin.setOnClickListener {
            startActivity(Intent(this, CheckinActivity::class.java))
        }

        if (intent.getBooleanExtra("open_checkin", false)) {
            startActivity(Intent(this, CheckinActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        setupUI()
        refreshCheckinState()
    }

    private fun setupUI() {
        val userName = Prefs.getUserName(this)
        val isHindi = Prefs.getLanguage(this) == "hi"

        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> if (isHindi) "सुप्रभात" else "Good morning"
            hour < 17 -> if (isHindi) "नमस्ते" else "Good afternoon"
            else      -> if (isHindi) "शुभ संध्या" else "Good evening"
        }
        binding.tvGreeting.text = "$greeting, $userName!"

        // Streak
        val streak = Prefs.getStreak(this)
        binding.tvStreakCount.text = streak.toString()
        binding.tvStreakLabel.text = if (streak == 1) {
            if (isHindi) "दिन की मालिका" else "day streak"
        } else {
            if (isHindi) "दिनों की मालिका" else "day streak"
        }

        // Quote
        val quote = QuoteRepository.getDailyMotivational(LocalDate.now().dayOfYear)
        binding.tvQuote.text = if (isHindi) quote.hi else quote.en

        // Family photo
        val photoUri = Prefs.getPhotoUri(this)
        if (photoUri.isNotEmpty()) {
            try {
                binding.imgFamilyPhoto.setImageURI(Uri.parse(photoUri))
                binding.imgFamilyPhoto.visibility = View.VISIBLE
            } catch (_: Exception) {
                binding.imgFamilyPhoto.visibility = View.GONE
            }
        }

        // Festival
        if (Prefs.isFestivalModeEnabled(this)) {
            val festival = FestivalHelper.getFestivalForDate(LocalDate.now())
            if (festival != null) {
                binding.cardFestival.visibility = View.VISIBLE
                binding.tvFestivalGreeting.text = "${festival.emoji} ${if (isHindi) festival.greetingHi else festival.greetingEn}"
                binding.tvFestivalTip.text = if (isHindi) festival.tipHi else festival.tipEn
            } else {
                binding.cardFestival.visibility = View.GONE
            }
        } else {
            binding.cardFestival.visibility = View.GONE
        }
    }

    private fun refreshCheckinState() {
        val isHindi = Prefs.getLanguage(this) == "hi"
        lifecycleScope.launch {
            val todayEntry = repo.getByDate(DateUtils.today())
            if (todayEntry != null) {
                binding.btnCheckin.isEnabled = false
                binding.btnCheckin.text = getString(R.string.home_checked_in_today)
                binding.tvCheckinStatus.visibility = View.VISIBLE
                binding.tvCheckinStatus.text = if (todayEntry.followedDiet) {
                    if (isHindi) "✅ आज का आहार: अच्छा" else "✅ Today's diet: Good"
                } else {
                    if (isHindi) "❌ आज का आहार: Not followed" else "❌ Today's diet: Not followed"
                }
            } else {
                binding.btnCheckin.isEnabled = true
                binding.btnCheckin.text = getString(R.string.home_btn_checkin)
                binding.tvCheckinStatus.visibility = View.GONE
            }
        }
    }
}