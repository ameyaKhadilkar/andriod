package com.example.sugarsaathi.ui.bloodsugar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sugarsaathi.R
import com.example.sugarsaathi.data.db.BloodSugarLog
import com.example.sugarsaathi.data.db.HbA1cLog
import com.example.sugarsaathi.databinding.ItemBloodSugarBinding
import com.example.sugarsaathi.databinding.ItemHba1cBinding
import com.example.sugarsaathi.util.DateUtils

class BloodSugarAdapter(private val onDelete: (BloodSugarLog) -> Unit) :
    ListAdapter<BloodSugarLog, BloodSugarAdapter.VH>(DiffCb()) {

    inner class VH(private val binding: ItemBloodSugarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(log: BloodSugarLog) {
            binding.tvDate.text = log.date
            binding.tvValue.text = "${log.value} mg/dL"
            binding.tvType.text = when (log.readingType) {
                "fasting"   -> binding.root.context.getString(R.string.bs_fasting)
                "post_meal" -> binding.root.context.getString(R.string.bs_post_meal)
                else        -> binding.root.context.getString(R.string.bs_random)
            }
            val (label, colorName) = DateUtils.bloodSugarLabel(log.value, log.readingType == "fasting")
            binding.tvStatus.text = label
            val color = when (colorName) {
                "color_good"    -> ContextCompat.getColor(binding.root.context, R.color.green_good)
                "color_warning" -> ContextCompat.getColor(binding.root.context, R.color.orange_warning)
                else            -> ContextCompat.getColor(binding.root.context, R.color.red_danger)
            }
            binding.tvStatus.setTextColor(color)
            binding.btnDelete.setOnClickListener { onDelete(log) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemBloodSugarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class DiffCb : DiffUtil.ItemCallback<BloodSugarLog>() {
        override fun areItemsTheSame(a: BloodSugarLog, b: BloodSugarLog) = a.id == b.id
        override fun areContentsTheSame(a: BloodSugarLog, b: BloodSugarLog) = a == b
    }
}

class HbA1cAdapter(private val onDelete: (HbA1cLog) -> Unit) :
    ListAdapter<HbA1cLog, HbA1cAdapter.VH>(DiffCb()) {

    inner class VH(private val binding: ItemHba1cBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(log: HbA1cLog) {
            binding.tvDate.text = log.date
            binding.tvValue.text = "${log.value}%"
            val (label, colorName) = DateUtils.hba1cLabel(log.value)
            binding.tvStatus.text = label
            val color = when (colorName) {
                "color_good"    -> ContextCompat.getColor(binding.root.context, R.color.green_good)
                "color_warning" -> ContextCompat.getColor(binding.root.context, R.color.orange_warning)
                else            -> ContextCompat.getColor(binding.root.context, R.color.red_danger)
            }
            binding.tvStatus.setTextColor(color)
            binding.btnDelete.setOnClickListener { onDelete(log) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemHba1cBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class DiffCb : DiffUtil.ItemCallback<HbA1cLog>() {
        override fun areItemsTheSame(a: HbA1cLog, b: HbA1cLog) = a.id == b.id
        override fun areContentsTheSame(a: HbA1cLog, b: HbA1cLog) = a == b
    }
}