package com.example.sugarsaathi.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sugarsaathi.ui.home.HomeActivity
import com.example.sugarsaathi.ui.onboarding.OnboardingActivity
import com.example.sugarsaathi.util.LocaleHelper
import com.example.sugarsaathi.util.Prefs

class SplashActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dest = if (Prefs.isOnboardingDone(this)) HomeActivity::class.java else OnboardingActivity::class.java
        startActivity(Intent(this, dest))
        finish()
    }
}