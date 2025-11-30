package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterSelfImprovementBinding
import com.raj.mygrowth.domain.ResponseSelfImprovementItem
import com.raj.mygrowth.interfaces.SimpleClick

class SelfImprovementAdapter(
    private val list: List<ResponseSelfImprovementItem>,
    click_: SimpleClick
) : RecyclerView.Adapter<SelfImprovementAdapter.ViewHolder>() {

    private val click = click_
    private val visiblePasswordPositions = mutableSetOf<Int>()

    class ViewHolder(val binding: AdapterSelfImprovementBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSelfImprovementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.item = item
        holder.binding.isVisible = visiblePasswordPositions.contains(position)

        holder.binding.root.setOnClickListener {
            if (visiblePasswordPositions.contains(position))
                visiblePasswordPositions.remove(position)
            else
                visiblePasswordPositions.add(position)

            notifyItemChanged(position)
            click.click(item.ai_link)
        }

        holder.binding.executePendingBindings()
    }
}
