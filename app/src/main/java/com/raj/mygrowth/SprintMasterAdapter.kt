package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterMasterSprintBinding
import com.raj.mygrowth.domain.SprintMasterItem

class SprintMasterAdapter(
    private val list: List<SprintMasterItem>,
    private val context: Context
) : RecyclerView.Adapter<SprintMasterAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterMasterSprintBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMasterSprintBinding.inflate(
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