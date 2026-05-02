package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterWorkoutBinding
import com.raj.mygrowth.domain.WorkoutResponse

class WorkoutAdapter(
    private val groups: WorkoutResponse,
) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = groups.workoutPlan.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = groups.workoutPlan[position]

        val formattedTitle = item.day.replaceFirstChar { it.uppercase() }

        holder.binding.tvDay.text = formattedTitle

        if (holder.binding.rvWorkout.layoutManager == null) {
            holder.binding.rvWorkout.layoutManager =
                LinearLayoutManager(holder.itemView.context)
        }

        if (holder.binding.rvWorkout.adapter == null) {
            holder.binding.rvWorkout.adapter = WorkoutAdapterItem(item.exercises)
        }
    }
}