package com.example.sugarsaathi.ui.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sugarsaathi.R
import com.example.sugarsaathi.databinding.ActivityOnboardingBinding
import com.example.sugarsaathi.notification.NotificationHelper
import com.example.sugarsaathi.ui.home.HomeActivity
import com.example.sugarsaathi.util.Prefs

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private var selectedPhotoUri: Uri? = null
    private var currentStep = 1

    private val photoPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedPhotoUri = it
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            binding.imgFamilyPhoto.setImageURI(it)
            binding.imgFamilyPhoto.visibility = View.VISIBLE
            binding.tvPhotoHint.text = getString(R.string.onboarding_photo_selected)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showStep(1)

        binding.btnPickPhoto.setOnClickListener {
            photoPicker.launch("image/*")
        }

        binding.btnLangEn.setOnClickListener { selectLanguage("en") }
        binding.btnLangHi.setOnClickListener { selectLanguage("hi") }

        binding.btnNext.setOnClickListener { goNext() }
        binding.btnFinish.setOnClickListener { finishOnboarding() }
    }

    private fun selectLanguage(lang: String) {
        Prefs.setLanguage(this, lang)
        binding.btnLangEn.isSelected = lang == "en"
        binding.btnLangHi.isSelected = lang == "hi"
    }

    private fun showStep(step: Int) {
        currentStep = step
        binding.stepWelcome.visibility = if (step == 1) View.VISIBLE else View.GONE
        binding.stepPhoto.visibility = if (step == 2) View.VISIBLE else View.GONE
        binding.stepNotification.visibility = if (step == 3) View.VISIBLE else View.GONE
        binding.btnNext.visibility = if (step < 3) View.VISIBLE else View.GONE
        binding.btnFinish.visibility = if (step == 3) View.VISIBLE else View.GONE
        binding.progressIndicator.progress = step
    }

    private fun goNext() {
        if (currentStep == 1) {
            val name = binding.etName.text.toString().trim()
            if (name.isEmpty()) {
                binding.tilName.error = getString(R.string.onboarding_name_required)
                return
            }
            binding.tilName.error = null
            Prefs.setUserName(this, name)
        }
        showStep(currentStep + 1)
    }

    private fun finishOnboarding() {
        // Save notification time
        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute
        Prefs.setNotificationTime(this, hour, minute)

        // Save photo
        selectedPhotoUri?.let { Prefs.setPhotoUri(this, it.toString()) }

        // Schedule nightly alarm
        NotificationHelper.scheduleNightlyCheckin(this)

        // Mark onboarding done
        Prefs.setOnboardingDone(this)

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}