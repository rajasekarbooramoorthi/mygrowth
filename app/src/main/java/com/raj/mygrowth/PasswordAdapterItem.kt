package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemPasswordBinding
import com.raj.mygrowth.domain.PasswordItem
import com.raj.mygrowth.interfaces.AdapterClick

class PasswordAdapterItem(
    private val list: List<PasswordItem>,
    private val click_: AdapterClick
) : RecyclerView.Adapter<PasswordAdapterItem.ViewHolder>() {

    private val visiblePasswordPositions = mutableSetOf<Int>()

    class ViewHolder(val binding: AdapterItemPasswordBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemPasswordBinding.inflate(
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
        }

        holder.binding.idEdit.setOnClickListener {
            click_.click(item.psw_sno)
        }

        holder.binding.executePendingBindings()
    }
}
