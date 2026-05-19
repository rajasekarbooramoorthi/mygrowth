package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterAttendanceGetItemBinding
import com.raj.mygrowth.domain.AttendanceGetItem
import com.raj.mygrowth.domain.RequestActionAddAttendance
import com.raj.mygrowth.interfaces.ClickAttendance

class AdapterGetAttendanceItem(
    private val list: List<AttendanceGetItem>,
    val clickAttendance: ClickAttendance

) : RecyclerView.Adapter<AdapterGetAttendanceItem.ViewHolder>() {
    class ViewHolder(val binding: AdapterAttendanceGetItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterAttendanceGetItemBinding.inflate(
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

        val name = item.name
        holder.binding.tvName.text = name
        holder.binding.checkBox.setOnCheckedChangeListener { _, bool ->
            val status = if (bool) 1 else 0
            clickAttendance.click(RequestActionAddAttendance("", status, item.id, ""))
        }
        holder.binding.ivPriority.setBackgroundResource(if (item.priority == 1) R.drawable.ic_priority_high else R.drawable.ic_priority_low)
    }
}
