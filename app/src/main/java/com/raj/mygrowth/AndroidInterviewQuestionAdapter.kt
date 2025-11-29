package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
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
    }
}
