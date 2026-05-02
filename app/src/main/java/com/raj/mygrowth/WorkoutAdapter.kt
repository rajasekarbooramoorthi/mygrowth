package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterWorkoutBinding
import com.raj.mygrowth.domain.WorkoutPlan

class WorkoutAdapter(
    private val list: List<WorkoutPlan>,
    private val context: Context
) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvDay.text = item.day.replaceFirstChar { it.uppercase() }
        holder.binding.tvMuscles.text = item.musclesWorked.joinToString(", ")

        if (holder.binding.rvWorkout.layoutManager == null) {
            holder.binding.rvWorkout.layoutManager =
                LinearLayoutManager(holder.itemView.context)
        }

        if (holder.binding.rvWorkout.adapter == null) {
            holder.binding.rvWorkout.adapter = WorkoutAdapterItem(item.exercises, context)
        }
    }
}