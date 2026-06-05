package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.ColorUtilities.colorsMulti
import com.raj.mygrowth.ColorUtilities.colorsMultiText
import com.raj.mygrowth.databinding.AdapterTaskSprintBinding
import com.raj.mygrowth.domain.SprintTaskItem
import com.raj.mygrowth.interfaces.SprintAdapterClick
import kotlin.math.abs

class SprintTaskAdapter(
    private val list: List<SprintTaskItem>,
    private val context: Context,
    val ClickID: SprintAdapterClick
) : RecyclerView.Adapter<SprintTaskAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterTaskSprintBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterTaskSprintBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        val formattedTitle = item.name.replaceFirstChar { it.uppercase() }

        holder.binding.tvName.text = formattedTitle
        holder.binding.tvDetail.text = item.details
        holder.binding.tvDescription.text = item.description
        holder.binding.tvId.text = "000" + item.id
        when (item.status) {
            "0" -> {
                holder.binding.tvStatus.text = "Yet to start"
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.orange_dark_50
                    )
                )
            }

            "1" -> {
                holder.binding.tvStatus.text = "In-Progress"
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue_grey_dark_50
                    )
                )
            }

            "2" -> {
                holder.binding.tvStatus.text = "Done"
                holder.binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.cyan_dark_50
                    )
                )
            }
        }

        holder.binding.viewLine.setBackgroundColor(
            ContextCompat.getColor(
                context,
                colorsMultiText[abs(position.hashCode()) % colorsMultiText.size]
            )
        )
        holder.binding.cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                colorsMulti[abs(position.hashCode()) % colorsMulti.size]
            )
        )
        holder.binding.tvEdit.setOnClickListener {
            ClickID.sprintTaskClick(item)
        }
    }
}