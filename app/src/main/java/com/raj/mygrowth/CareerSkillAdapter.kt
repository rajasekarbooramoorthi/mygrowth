package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemCareerMasterSkillBinding
import com.raj.mygrowth.domain.CareerMaster
import com.raj.mygrowth.interfaces.SimpleClick

class CareerSkillAdapter(
    private val list: List<CareerMaster>, click_: SimpleClick
) : RecyclerView.Adapter<CareerSkillAdapter.ViewHolder>() {
    var click = click_

    class ViewHolder(val binding: AdapterItemCareerMasterSkillBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemCareerMasterSkillBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.item = item
        holder.binding.root.setOnClickListener {
           click.click(item.id,item.name, item.type)
        }
        holder.binding.executePendingBindings()
    }
}
