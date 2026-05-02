package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.WorkoutAdapterItemBinding
import com.raj.mygrowth.domain.WorkoutPlanItem

class WorkoutAdapterItem(
    private val list: List<WorkoutPlanItem>,
) : RecyclerView.Adapter<WorkoutAdapterItem.ViewHolder>() {

    private val colors = listOf(
        R.color.red_light,
        R.color.blue_light,
        R.color.green_light,
        R.color.yellow_light,
        R.color.purple_light,
        R.color.orange_light,
        R.color.cyan_light
    )


    class ViewHolder(val binding: WorkoutAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WorkoutAdapterItemBinding.inflate(
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

        val name = item.name.capitalizeWords()
        val description = item.notes.capitalizeWords()
        holder.binding.tvName.text = name
        //holder.tvTaskDescription.text = description

        /*        val index = abs(item.dt_sno.hashCode()) % colors.size
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
                }*/
    }
}
