package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.domain.DailyTaskItem
import com.raj.mygrowth.interfaces.SimpleClick

class DailyTaskAdapterItem(
    private val list: List<DailyTaskItem>,
    listener_: SimpleClick
) : RecyclerView.Adapter<DailyTaskAdapterItem.ViewHolder>() {

    val listener = listener_

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTaskName: TextView = view.findViewById(R.id.tvTaskName)
        val tvTaskDescription: TextView = view.findViewById(R.id.tvTaskDescription)
        val tvDueDate: TextView = view.findViewById(R.id.tvDueDate)
        val checkBox: TextView = view.findViewById(R.id.chk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_daily_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        fun String.capitalizeWords(): String {
            return this.split(" ")
                .joinToString(" ") { word ->
                    word.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    }
                }
        }
        val name = item.dt_name.capitalizeWords()
        val description = item.dt_description.capitalizeWords()

        holder.tvTaskName.text = name
        holder.tvTaskDescription.text = description
        holder.tvDueDate.text = "Due: ${item.dt_due_date}"
        holder.checkBox.setOnClickListener {
            listener.checkCompleted(item.dt_sno)
        }
    }
}
