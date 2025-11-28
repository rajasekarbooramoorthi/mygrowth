package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.domain.DailyTask

class DailyTaskAdapter(
    private val groups: List<DailyTask>
) : RecyclerView.Adapter<DailyTaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTaskName: TextView = view.findViewById(R.id.tvTaskName)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvDailyTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_daily_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = groups.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = groups[position]

        holder.tvTaskName.text = item.taskName

        // Set layout manager once
        holder.recyclerView.layoutManager =
            LinearLayoutManager(holder.itemView.context)

        // Set inner adapter
        holder.recyclerView.adapter = DailyTaskAdapterItem(item.list)
    }
}
