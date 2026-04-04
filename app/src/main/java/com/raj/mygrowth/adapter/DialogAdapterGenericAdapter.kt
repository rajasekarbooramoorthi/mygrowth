package com.raj.mygrowth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.DialogItemAdapterBinding
import com.raj.mygrowth.interfaces.MasterInterFace


class DialogAdapterGenericAdapter(
    private val list: List<String>,
    click_: MasterInterFace,
    path_: String
) : RecyclerView.Adapter<DialogAdapterGenericAdapter.ViewHolder>() {
    var click = click_
    var path = path_

    class ViewHolder(val binding: DialogItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DialogItemAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvName.text = item
        holder.binding.root.setOnClickListener {
            click.loadUrl(item, path)
        }
        holder.binding.executePendingBindings()
    }
}