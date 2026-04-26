package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.domain.DailyTaskItem
import com.raj.mygrowth.interfaces.SimpleClick
import kotlin.math.abs

class DailyTaskAdapterItem(
    private val list: List<DailyTaskItem>,
    listener_: SimpleClick,
    context_: Context,
) : RecyclerView.Adapter<DailyTaskAdapterItem.ViewHolder>() {

    val listener = listener_
    val context = context_

    private val colors = listOf(
        R.color.red_light,
        R.color.blue_light,
        R.color.green_light,
        R.color.yellow_light,
        R.color.purple_light,
        R.color.orange_light,
        R.color.cyan_light
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTaskName: TextView = view.findViewById(R.id.tvTaskName)
        val cardView: CardView = view.findViewById(R.id.cardView)
        val tvTaskDescription: TextView = view.findViewById(R.id.tvTaskDescription)
        val ivPriority: AppCompatImageView = view.findViewById(R.id.ivPriority)

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
        val index = abs(item.dt_sno.hashCode()) % colors.size
        val colorRes = colors[index]

        // holder.cardView.setCardBackgroundColor( ContextCompat.getColor(context, colorRes) )
        if (item.dt_priority == 1) {
            holder.ivPriority.setBackgroundResource(R.drawable.ic_priority_high)
        } else {
            holder.ivPriority.setBackgroundResource(R.drawable.ic_priority_low)
        }
        if (item.dt_status == 1) {
            holder.checkBox.visibility = GONE
        } else {
            holder.checkBox.visibility = VISIBLE
        }
    }
}
