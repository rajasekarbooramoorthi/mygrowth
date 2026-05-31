package com.raj.mygrowth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.ColorUtilities.colorsMulti
import com.raj.mygrowth.ColorUtilities.colorsMultiText
import com.raj.mygrowth.databinding.AdapterQuitZillaMotivationBinding
import com.raj.mygrowth.domain.ResponseQuitZillaMotivationItem
import kotlin.math.abs

class QuitZillaMotivationAdapter(
    private val list: List<ResponseQuitZillaMotivationItem>,
    private val context: Context,
) : RecyclerView.Adapter<QuitZillaMotivationAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterQuitZillaMotivationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterQuitZillaMotivationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        val formattedTitle = item.description.replaceFirstChar { it.uppercase() }

        holder.binding.tvName.text = formattedTitle
        holder.binding.tvDetail.text = (item.title)
        holder.binding.tvDescription.text = item.description

        val index = abs(position.hashCode()) % colorsMulti.size
        val colorRes = colorsMulti[index]

        val indexs = abs(position.hashCode()) % colorsMultiText.size
        val colorRess = colorsMultiText[indexs]

        holder.binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, colorRess))
        holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
    }
}