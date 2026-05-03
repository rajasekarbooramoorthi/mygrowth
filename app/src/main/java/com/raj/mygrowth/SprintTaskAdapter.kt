package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterTaskSprintBinding
import com.raj.mygrowth.domain.SprintTaskItem

class SprintTaskAdapter(
    private val list: List<SprintTaskItem>,
    private val context: Context
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

        //val formattedTitle = item.date.replaceFirstChar { it.uppercase() }

        holder.binding.tvName.text = item.name
        holder.binding.tvDescription.text = item.description

    }
}