package com.example.sugarsaathi.ui.doctor

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.db.DoctorVisit
import com.example.sugarsaathi.databinding.ActivityDoctorBinding
import com.example.sugarsaathi.databinding.DialogAddDoctorVisitBinding
import com.example.sugarsaathi.util.DateUtils
import kotlinx.coroutines.launch

class DoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorBinding
    private val db by lazy { AppDatabase.get(this) }
    private lateinit var adapter: DoctorVisitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.doctor_title)
        toolbar.setNavigationOnClickListener { finish() }

        adapter = DoctorVisitAdapter { visit -> lifecycleScope.launch { db.doctorVisitDao().delete(visit) } }
        binding.rvVisits.layoutManager = LinearLayoutManager(this)
        binding.rvVisits.adapter = adapter

        db.doctorVisitDao().getAll().observe(this) { adapter.submitList(it) }
        binding.fabAddVisit.setOnClickListener { showAddVisitDialog() }
    }

    private fun showAddVisitDialog() {
        val dialogBinding = DialogAddDoctorVisitBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle(R.string.doctor_add_visit)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_save) { _, _ ->
                val doctor = dialogBinding.etDoctorName.text.toString().trim()
                if (doctor.isEmpty()) return@setPositiveButton
                lifecycleScope.launch {
                    db.doctorVisitDao().insert(DoctorVisit(
                        date = dialogBinding.etDate.text.toString().ifEmpty { DateUtils.today() },
                        doctorName = doctor,
                        notes = dialogBinding.etNotes.text.toString().trim(),
                        nextVisitDate = dialogBinding.etNextVisit.text.toString().trim()
                    ))
                }
            }
            .setNegativeButton(R.string.btn_cancel, null).show()
    }
}