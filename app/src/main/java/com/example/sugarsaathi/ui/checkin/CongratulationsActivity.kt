package com.example.sugarsaathi.ui.checkin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sugarsaathi.R
import com.example.sugarsaathi.databinding.ActivityCongratulationsBinding
import com.example.sugarsaathi.ui.home.HomeActivity
import com.example.sugarsaathi.util.Prefs
import com.example.sugarsaathi.util.QuoteRepository

class CongratulationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCongratulationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCongratulationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
            finish()
        }

        val streak = Prefs.getStreak(this)
        val isHindi = Prefs.getLanguage(this) == "hi"
        val quote = QuoteRepository.getRandomCongratulatoryQuote()

        binding.tvStreakValue.text = streak.toString()
        binding.tvStreakLabel.text = if (streak == 1) {
            if (isHindi) "दिन की मालिका!" else "day streak!"
        } else {
            if (isHindi) "दिनों की मालिका!" else "day streak!"
        }
        binding.tvQuote.text = if (isHindi) quote.hi else quote.en

        binding.btnDone.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
            finish()
        }
    }
}