package com.example.sugarsaathi.ui.bloodsugar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.db.BloodSugarLog
import com.example.sugarsaathi.data.db.HbA1cLog
import com.example.sugarsaathi.databinding.ActivityBloodSugarBinding
import com.example.sugarsaathi.databinding.DialogAddBloodSugarBinding
import com.example.sugarsaathi.databinding.DialogAddHba1cBinding
import com.example.sugarsaathi.util.DateUtils
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class BloodSugarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBloodSugarBinding
    private val db by lazy { AppDatabase.get(this) }
    private lateinit var bsAdapter: BloodSugarAdapter
    private lateinit var hba1cAdapter: HbA1cAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodSugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.bloodsugar_title)
        toolbar.setNavigationOnClickListener { finish() }

        bsAdapter = BloodSugarAdapter { log -> lifecycleScope.launch { db.bloodSugarDao().delete(log) } }
        hba1cAdapter = HbA1cAdapter { log -> lifecycleScope.launch { db.hbA1cDao().delete(log) } }
        binding.rvBloodSugar.adapter = bsAdapter
        binding.rvHba1c.adapter = hba1cAdapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.rvBloodSugar.visibility = if (tab.position == 0) View.VISIBLE else View.GONE
                binding.rvHba1c.visibility = if (tab.position == 1) View.VISIBLE else View.GONE
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.fabAdd.setOnClickListener {
            if (binding.tabLayout.selectedTabPosition == 0) showAddBloodSugarDialog()
            else showAddHbA1cDialog()
        }

        db.bloodSugarDao().getAll().observe(this) { bsAdapter.submitList(it) }
        db.hbA1cDao().getAll().observe(this) { hba1cAdapter.submitList(it) }
    }

    private fun showAddBloodSugarDialog() {
        val dialogBinding = DialogAddBloodSugarBinding.inflate(LayoutInflater.from(this))
        val types = arrayOf(getString(R.string.bs_fasting), getString(R.string.bs_post_meal), getString(R.string.bs_random))
        dialogBinding.spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        AlertDialog.Builder(this)
            .setTitle(R.string.bs_add_reading)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_save) { _, _ ->
                val value = dialogBinding.etValue.text.toString().toIntOrNull() ?: return@setPositiveButton
                val typeKey = when (dialogBinding.spinnerType.selectedItemPosition) { 0 -> "fasting"; 1 -> "post_meal"; else -> "random" }
                lifecycleScope.launch { db.bloodSugarDao().insert(BloodSugarLog(date = DateUtils.today(), value = value, readingType = typeKey)) }
            }
            .setNegativeButton(R.string.btn_cancel, null).show()
    }

    private fun showAddHbA1cDialog() {
        val dialogBinding = DialogAddHba1cBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle(R.string.hba1c_add)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_save) { _, _ ->
                val value = dialogBinding.etValue.text.toString().toFloatOrNull() ?: return@setPositiveButton
                val date = dialogBinding.etDate.text.toString().ifEmpty { DateUtils.today() }
                lifecycleScope.launch { db.hbA1cDao().insert(HbA1cLog(date = date, value = value)) }
            }
            .setNegativeButton(R.string.btn_cancel, null).show()
    }
}