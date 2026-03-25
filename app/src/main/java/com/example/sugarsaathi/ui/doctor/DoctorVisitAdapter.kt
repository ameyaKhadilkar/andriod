package com.example.sugarsaathi.ui.doctor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sugarsaathi.data.db.DoctorVisit
import com.example.sugarsaathi.databinding.ItemDoctorVisitBinding

class DoctorVisitAdapter(private val onDelete: (DoctorVisit) -> Unit) :
    ListAdapter<DoctorVisit, DoctorVisitAdapter.VH>(DiffCb()) {

    inner class VH(private val binding: ItemDoctorVisitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(visit: DoctorVisit) {
            binding.tvDate.text = visit.date
            binding.tvDoctorName.text = visit.doctorName
            binding.tvNotes.text = visit.notes.ifEmpty { "—" }
            if (visit.nextVisitDate.isNotEmpty()) {
                binding.tvNextVisit.visibility = View.VISIBLE
                binding.tvNextVisit.text = "Next visit: ${visit.nextVisitDate}"
            } else {
                binding.tvNextVisit.visibility = View.GONE
            }
            binding.btnDelete.setOnClickListener { onDelete(visit) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemDoctorVisitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class DiffCb : DiffUtil.ItemCallback<DoctorVisit>() {
        override fun areItemsTheSame(a: DoctorVisit, b: DoctorVisit) = a.id == b.id
        override fun areContentsTheSame(a: DoctorVisit, b: DoctorVisit) = a == b
    }
}