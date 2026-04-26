package com.raj.mygrowth.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.R
import com.raj.mygrowth.databinding.CategoryMainAdapterBinding
import com.raj.mygrowth.domain.Category
import com.raj.mygrowth.interfaces.MasterInterFace


class MyCategoryMainAdapter(
    private val list: List<Category>,
    click_: MasterInterFace,
    context_: Context,
) : RecyclerView.Adapter<MyCategoryMainAdapter.ViewHolder>() {
    var click = click_
    var context = context_

    private var selectedPosition = 0

    class ViewHolder(val binding: CategoryMainAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryMainAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(
        holder: ViewHolder, @SuppressLint("RecyclerView") position: Int
    ) {
        val item = list[position]
        if (position == selectedPosition) {
            holder.binding.linearName.setBackgroundResource(R.drawable.bg_item_selected)
        } else {
            holder.binding.linearName.setBackgroundResource(R.drawable.bg_item_normal)
        }
        holder.binding.tvName.text = item.categoryName

        holder.binding.linearName.setOnClickListener {

            val previous = selectedPosition
            selectedPosition = position

            if (previous != RecyclerView.NO_POSITION) {
                notifyItemChanged(previous)
            }
            notifyItemChanged(selectedPosition)
            click.clickSubCategory(item.subcategoryList)
        }
        holder.binding.executePendingBindings()
    }
}
