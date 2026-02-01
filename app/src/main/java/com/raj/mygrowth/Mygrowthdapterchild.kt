package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterMyGrowthChildBinding
import com.raj.mygrowth.domain.ResponseGenericItem
import com.raj.mygrowth.interfaces.SimpleClick

class Mygrowthdapterchild(
    private val list: List<ResponseGenericItem>, click_: SimpleClick
) : RecyclerView.Adapter<Mygrowthdapterchild.ViewHolder>() {
    var click = click_
    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(val binding: AdapterMyGrowthChildBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMyGrowthChildBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.item = item
        if (position == selectedPosition) {
            holder.binding.linearName.setBackgroundResource(R.drawable.bg_item_selected)
        } else {
            holder.binding.linearName.setBackgroundResource(R.drawable.bg_child_item_normal)
        }
        holder.binding.root.setOnClickListener {
            val previous = selectedPosition
            selectedPosition = position
            if (previous != RecyclerView.NO_POSITION) {
                notifyItemChanged(previous)
            }
            notifyItemChanged(selectedPosition)
            click.actionID(item.tag, item.id,item.folder)
        }
        holder.binding.executePendingBindings()
    }
}
