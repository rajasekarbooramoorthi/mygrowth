package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterAttendanceItemBinding
import com.raj.mygrowth.domain.AttendanceItem

class AdapterAttendanceItem(
    private val list: List<AttendanceItem>, private val context: Context

) : RecyclerView.Adapter<AdapterAttendanceItem.ViewHolder>() {


    class ViewHolder(val binding: AdapterAttendanceItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterAttendanceItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        fun String.capitalizeWords(): String {
            return this.split(" ").joinToString(" ") { word ->
                word.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }
            }
        }

        val name = item.name.capitalizeWords()
        holder.binding.tvName.text = name
        holder.binding.chkName.isChecked = item.status == 1
    }
}
