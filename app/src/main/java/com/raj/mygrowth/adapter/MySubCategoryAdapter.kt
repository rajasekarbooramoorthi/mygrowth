package com.raj.mygrowth.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.R
import com.raj.mygrowth.databinding.SubCategoryAdpterBinding
import com.raj.mygrowth.domain.SubCategory
import com.raj.mygrowth.interfaces.MasterInterFace


class MySubCategoryAdapter(
    private val list: List<SubCategory>, click_: MasterInterFace
) : RecyclerView.Adapter<MySubCategoryAdapter.ViewHolder>() {
    var click = click_
    private var selectedPosition = 0

    class ViewHolder(val binding: SubCategoryAdpterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SubCategoryAdpterBinding.inflate(
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
            holder.binding.linearName.setBackgroundResource(R.drawable.bg_selected_sub_category)
        } else {
            holder.binding.linearName.setBackgroundResource(R.drawable.bg_child_item_normal)
        }
        holder.binding.tvName.text = item.subcategoryName
        holder.binding.root.setOnClickListener {
            val previous = selectedPosition
            selectedPosition = position
            if (previous != RecyclerView.NO_POSITION) {
                notifyItemChanged(previous)
            }
            notifyItemChanged(selectedPosition)
            try {
                click.clickItem(item.itemList, item.folderName ?: "", item.filetype ?: "")
            } catch (e: Exception) {
                println("Exception-->" + e)
            }
        }
        holder.binding.executePendingBindings()
    }
}
