package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemAndroidInterviewBinding
import com.raj.mygrowth.domain.AndroidInterviewItem

class AndroidInterviewQuestionAdapter(
    private val list: List<AndroidInterviewItem>
) : RecyclerView.Adapter<AndroidInterviewQuestionAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterItemAndroidInterviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemAndroidInterviewBinding.inflate(
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
        holder.binding.executePendingBindings()

        // Avoid creating LayoutManager & Adapter repeatedly on scroll
        if (holder.binding.recyclerView.layoutManager == null) {
            holder.binding.recyclerView.layoutManager =
                LinearLayoutManager(holder.itemView.context)
        }

        // Set adapter only if not set already
        if (holder.binding.recyclerView.adapter == null) {
            holder.binding.recyclerView.adapter = AndroidInterviewQuestionLinkAdapter(item.links)
        }
    }
}
