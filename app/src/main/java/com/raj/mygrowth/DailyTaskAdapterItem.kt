package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.domain.DailyTaskItem

class DailyTaskAdapterItem(
    private val list: List<DailyTaskItem>
) : RecyclerView.Adapter<DailyTaskAdapterItem.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTaskName: TextView = view.findViewById(R.id.tvTaskName)
        val tvDueDate: TextView = view.findViewById(R.id.tvDueDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_daily_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvTaskName.text = item.dt_name
        holder.tvDueDate.text = "Due: ${item.dt_due_date}"
    }
}
