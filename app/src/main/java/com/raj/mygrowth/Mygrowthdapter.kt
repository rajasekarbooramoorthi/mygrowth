package com.raj.mygrowth

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterMyGrowthBinding
import com.raj.mygrowth.domain.ResponseGenericItem
import com.raj.mygrowth.interfaces.SimpleClick

class Mygrowthdapter(
    private val list: List<ResponseGenericItem>, click_: SimpleClick
) : RecyclerView.Adapter<Mygrowthdapter.ViewHolder>() {
    var click = click_
    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(val binding: AdapterMyGrowthBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMyGrowthBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = list[position]
        holder.binding.item = item
        if (position == selectedPosition) {
            holder.binding.linearName.setBackgroundResource(R.drawable.bg_item_selected)
        } else {
            holder.binding.linearName.setBackgroundResource(R.drawable.bg_item_normal)
        }
        holder.binding.root.setOnClickListener {

            val previous = selectedPosition
            selectedPosition = position

            if (previous != RecyclerView.NO_POSITION) {
                notifyItemChanged(previous)
            }
            notifyItemChanged(selectedPosition)
            click.callApi(item.tag)
        }
        holder.binding.executePendingBindings()
    }
}
