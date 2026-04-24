package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.domain.DailyTask
import com.raj.mygrowth.interfaces.SimpleClick

class DailyTaskAdapter(
    private val groups: List<DailyTask>, listener_: SimpleClick
) : RecyclerView.Adapter<DailyTaskAdapter.ViewHolder>() {

    private val listener = listener_
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

        // Capitalize first letter only — optimized
        val formattedTitle = item.taskName.replaceFirstChar { it.uppercase() }

        holder.tvTaskName.text = formattedTitle

        // Avoid creating LayoutManager & Adapter repeatedly on scroll
        if (holder.recyclerView.layoutManager == null) {
            holder.recyclerView.layoutManager =
                LinearLayoutManager(holder.itemView.context)
        }




        holder.tvTaskName
        // Set adapter only if not set already
        if (holder.recyclerView.adapter == null) {
            holder.recyclerView.adapter = DailyTaskAdapterItem(item.list, listener)
        }
    }
}