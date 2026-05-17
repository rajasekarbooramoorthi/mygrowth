package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.ColorUtilities.colorsMulti
import com.raj.mygrowth.databinding.AdapterTaskSprintBinding
import com.raj.mygrowth.domain.SprintTaskItem
import kotlin.math.abs

class SprintTaskAdapter(
    private val list: List<SprintTaskItem>,
    private val context: Context,
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

        val index = abs(position.hashCode()) % colorsMulti.size
        val colorRes = colorsMulti[index]
        holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
    }
}