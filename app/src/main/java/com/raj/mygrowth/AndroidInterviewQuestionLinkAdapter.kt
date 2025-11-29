package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemAndroidInterviewLinkBinding

class AndroidInterviewQuestionLinkAdapter(
    private val list: ArrayList<String>
) : RecyclerView.Adapter<AndroidInterviewQuestionLinkAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterItemAndroidInterviewLinkBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemAndroidInterviewLinkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvName.text = item

    }
}
