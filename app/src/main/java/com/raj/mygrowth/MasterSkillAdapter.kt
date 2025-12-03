package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemMasterSkillBinding
import com.raj.mygrowth.domain.AndroidMaster
import com.raj.mygrowth.interfaces.SimpleClick

class MasterSkillAdapter(
    private val list: List<AndroidMaster>, click_: SimpleClick
) : RecyclerView.Adapter<MasterSkillAdapter.ViewHolder>() {
    var click = click_

    class ViewHolder(val binding: AdapterItemMasterSkillBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemMasterSkillBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.item = item
        holder.binding.root.setOnClickListener {
            click.click(item.ms_tag,item.ms_folder,item.ms_type)
        }
        holder.binding.executePendingBindings()
    }
}
