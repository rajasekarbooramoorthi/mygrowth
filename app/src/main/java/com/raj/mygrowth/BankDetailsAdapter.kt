package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemBankDetailsBinding
import com.raj.mygrowth.domain.BankItem

class BankDetailsAdapter(
    private val list: List<BankItem>
) : RecyclerView.Adapter<BankDetailsAdapter.ViewHolder>() {

    private val visiblePasswordPositions = mutableSetOf<Int>()

    class ViewHolder(val binding: AdapterItemBankDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemBankDetailsBinding.inflate(
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

        holder.binding.executePendingBindings()
    }
}
