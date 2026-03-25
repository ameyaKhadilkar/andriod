package com.example.sugarsaathi.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.DataExportManager
import com.example.sugarsaathi.databinding.ActivitySettingsBinding
import com.example.sugarsaathi.notification.NotificationHelper
import com.example.sugarsaathi.util.DateUtils
import com.example.sugarsaathi.util.LocaleHelper
import com.example.sugarsaathi.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val exportManager by lazy { DataExportManager(this) }

    // File pickers
    private val createFileLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        uri ?: return@registerForActivityResult
        lifecycleScope.launch {
            try {
                val json = withContext(Dispatchers.IO) { exportManager.toJson() }
                contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
                Toast.makeText(this@SettingsActivity, getString(R.string.export_success), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, getString(R.string.export_fail), Toast.LENGTH_LONG).show()
            }
        }
    }

    private val openFileLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri ?: return@registerForActivityResult
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.import_confirm_title))
            .setMessage(getString(R.string.import_confirm_msg))
            .setPositiveButton(getString(R.string.import_confirm_yes)) { _, _ ->
                lifecycleScope.launch {
                    try {
                        val json = withContext(Dispatchers.IO) {
                            contentResolver.openInputStream(uri)?.bufferedReader()?.readText() ?: ""
                        }
                        val result = withContext(Dispatchers.IO) { exportManager.fromJson(json) }
                        when (result) {
                            is DataExportManager.ImportResult.Success ->
                                Toast.makeText(this@SettingsActivity,
                                    getString(R.string.import_success, result.exportDate), Toast.LENGTH_LONG).show()
                            is DataExportManager.ImportResult.Error ->
                                Toast.makeText(this@SettingsActivity,
                                    getString(R.string.import_fail, result.message), Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@SettingsActivity,
                            getString(R.string.import_fail, e.message), Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.settings_title)
        toolbar.setNavigationOnClickListener { finish() }

        loadCurrentSettings()

        binding.btnLangEn.setOnClickListener { setLanguage("en") }
        binding.btnLangHi.setOnClickListener { setLanguage("hi") }

        binding.switchFestivalMode.setOnCheckedChangeListener { _, checked -> Prefs.setFestivalMode(this, checked) }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isNotEmpty()) Prefs.setUserName(this, name)
            Prefs.setNotificationTime(this, binding.timePicker.hour, binding.timePicker.minute)
            NotificationHelper.scheduleNightlyCheckin(this)
            finish()
        }

        binding.btnExport.setOnClickListener {
            val fileName = "SugarSaathi_backup_${DateUtils.today()}.json"
            createFileLauncher.launch(fileName)
        }

        binding.btnImport.setOnClickListener {
            openFileLauncher.launch(arrayOf("application/json", "*/*"))
        }
    }

    private fun loadCurrentSettings() {
        binding.etName.setText(Prefs.getUserName(this))
        binding.timePicker.hour = Prefs.getNotificationHour(this)
        binding.timePicker.minute = Prefs.getNotificationMinute(this)
        binding.switchFestivalMode.isChecked = Prefs.isFestivalModeEnabled(this)
        val lang = Prefs.getLanguage(this)
        binding.btnLangEn.isSelected = lang == "en"
        binding.btnLangHi.isSelected = lang == "hi"
    }

    private fun setLanguage(lang: String) {
        LocaleHelper.setLocale(this, lang)
        binding.btnLangEn.isSelected = lang == "en"
        binding.btnLangHi.isSelected = lang == "hi"
        recreate()
    }
}