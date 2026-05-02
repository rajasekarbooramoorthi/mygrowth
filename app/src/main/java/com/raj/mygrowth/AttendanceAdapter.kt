package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterAttendanceBinding
import com.raj.mygrowth.domain.ResponseAttendance
import com.raj.mygrowth.domain.WorkoutResponse

class AttendanceAdapter(
    private val response: ResponseAttendance,
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterAttendanceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = response.data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = response.data[position]

        val formattedTitle = item.name .replaceFirstChar { it.uppercase() }
        holder.binding.tvName.text = formattedTitle
        holder.binding.tvDate.text = item.date

    }
}