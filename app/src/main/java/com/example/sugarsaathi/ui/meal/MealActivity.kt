package com.example.sugarsaathi.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.AppDatabase
import com.example.sugarsaathi.data.db.MealLog
import com.example.sugarsaathi.databinding.ActivityMealBinding
import com.example.sugarsaathi.databinding.DialogAddMealBinding
import com.example.sugarsaathi.util.DateUtils
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private val db by lazy { AppDatabase.get(this) }

    private val quickFoods = listOf(
        "Roti", "Rice", "Dal", "Sabzi", "Salad", "Curd", "Khichdi",
        "Poha", "Upma", "Idli", "Dosa", "Paratha", "Rajma", "Chana",
        "Paneer", "Egg", "Chicken", "Fish", "Soup", "Fruits"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.meal_title)
        toolbar.setNavigationOnClickListener { finish() }

        binding.btnAddBreakfast.setOnClickListener { showAddMealDialog("breakfast") }
        binding.btnAddLunch.setOnClickListener { showAddMealDialog("lunch") }
        binding.btnAddSnack.setOnClickListener { showAddMealDialog("snack") }
        binding.btnAddDinner.setOnClickListener { showAddMealDialog("dinner") }

        observeMeals()
    }

    private fun observeMeals() {
        db.mealLogDao().getByDate(DateUtils.today()).observe(this) { meals ->
            val nothing = getString(R.string.meal_nothing_logged)
            binding.tvBreakfastItems.text = meals.filter { it.mealType == "breakfast" }.joinToString(", ") { it.foodItems }.ifEmpty { nothing }
            binding.tvLunchItems.text    = meals.filter { it.mealType == "lunch" }.joinToString(", ") { it.foodItems }.ifEmpty { nothing }
            binding.tvSnackItems.text    = meals.filter { it.mealType == "snack" }.joinToString(", ") { it.foodItems }.ifEmpty { nothing }
            binding.tvDinnerItems.text   = meals.filter { it.mealType == "dinner" }.joinToString(", ") { it.foodItems }.ifEmpty { nothing }
        }
    }

    private fun showAddMealDialog(mealType: String) {
        val dialogBinding = DialogAddMealBinding.inflate(LayoutInflater.from(this))
        quickFoods.forEach { food ->
            val chip = Chip(this).apply { text = food; isCheckable = true }
            dialogBinding.chipGroupFoods.addView(chip)
        }
        val titleRes = when (mealType) {
            "breakfast" -> R.string.meal_breakfast; "lunch" -> R.string.meal_lunch
            "snack"     -> R.string.meal_snack;     else   -> R.string.meal_dinner
        }
        AlertDialog.Builder(this)
            .setTitle(titleRes)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_log) { _, _ ->
                val selected = mutableListOf<String>()
                for (i in 0 until dialogBinding.chipGroupFoods.childCount) {
                    val chip = dialogBinding.chipGroupFoods.getChildAt(i) as? Chip
                    if (chip?.isChecked == true) selected.add(chip.text.toString())
                }
                val custom = dialogBinding.etCustomFood.text.toString().trim()
                if (custom.isNotEmpty()) selected.add(custom)
                if (selected.isEmpty()) return@setPositiveButton
                lifecycleScope.launch {
                    db.mealLogDao().insert(MealLog(date = DateUtils.today(), mealType = mealType, foodItems = selected.joinToString(", "), isHealthy = dialogBinding.cbIsHealthy.isChecked))
                }
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }
}