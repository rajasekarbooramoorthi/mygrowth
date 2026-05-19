package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.ColorUtilities.colorsMulti
import com.raj.mygrowth.databinding.AdapterAttendanceBinding
import com.raj.mygrowth.domain.AttendanceData
import com.raj.mygrowth.interfaces.ClickAttendance
import kotlin.math.abs

class AttendanceAdapter(
    private val list: List<AttendanceData>,
    private val context: Context,
    private val clickAttendance: ClickAttendance
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterAttendanceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        //val formattedTitle = item.date.replaceFirstChar { it.uppercase() }

        holder.binding.tvDate.text = item.date

        if (holder.binding.recyclerView.layoutManager == null) {
            holder.binding.recyclerView.layoutManager =
                LinearLayoutManager(holder.itemView.context)
        }

        if (holder.binding.recyclerView.adapter == null) {
            holder.binding.recyclerView.adapter =
                AdapterGetAttendanceItem(item.list, clickAttendance)
        }

        val index = abs(position.hashCode()) % colorsMulti.size
        val colorRes = colorsMulti[index]
        // holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
    }
}