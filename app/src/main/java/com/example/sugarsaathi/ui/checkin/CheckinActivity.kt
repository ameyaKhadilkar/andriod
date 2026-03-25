package com.example.sugarsaathi.ui.checkin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.repository.CheckInRepository
import com.example.sugarsaathi.databinding.ActivityCheckinBinding
import kotlinx.coroutines.launch

class CheckinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckinBinding
    private lateinit var repo: CheckInRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.checkin_title)
        toolbar.setNavigationOnClickListener { finish() }

        repo = CheckInRepository(AppDatabase.get(this), this)

        binding.btnYes.setOnClickListener {
            lifecycleScope.launch {
                repo.checkinToday(true)
                startActivity(Intent(this@CheckinActivity, CongratulationsActivity::class.java))
                finish()
            }
        }

        binding.btnNo.setOnClickListener {
            startActivity(Intent(this, ReflectionActivity::class.java))
            finish()
        }
    }
}