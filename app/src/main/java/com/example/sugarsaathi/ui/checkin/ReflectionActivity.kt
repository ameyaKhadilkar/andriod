package com.example.sugarsaathi.ui.checkin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.repository.CheckInRepository
import com.example.sugarsaathi.databinding.ActivityReflectionBinding
import com.example.sugarsaathi.ui.home.HomeActivity
import kotlinx.coroutines.launch

class ReflectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReflectionBinding
    private lateinit var repo: CheckInRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReflectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.reflection_nav_title)
        toolbar.setNavigationOnClickListener { finish() }

        repo = CheckInRepository(AppDatabase.get(this), this)

        binding.btnSubmit.setOnClickListener {
            val notes = binding.etNotes.text.toString().trim()
            lifecycleScope.launch { repo.checkinToday(false, notes); goHome() }
        }
        binding.btnSkip.setOnClickListener {
            lifecycleScope.launch { repo.checkinToday(false); goHome() }
        }
    }

    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        finish()
    }
}