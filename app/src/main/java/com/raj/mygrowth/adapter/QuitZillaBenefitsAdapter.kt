package com.raj.mygrowth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.ColorUtilities.colorsMulti
import com.raj.mygrowth.ColorUtilities.colorsMultiText
import com.raj.mygrowth.databinding.AdapterQuitZillaBenefitsBinding
import com.raj.mygrowth.domain.ResponseQuitZillaBenefitsItem
import kotlin.math.abs

class QuitZillaBenefitsAdapter(
    private val list: List<ResponseQuitZillaBenefitsItem>,
    private val context: Context,
) : RecyclerView.Adapter<QuitZillaBenefitsAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterQuitZillaBenefitsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterQuitZillaBenefitsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        val formattedTitle = item.description.replaceFirstChar { it.uppercase() }
        val formattedBenefits = item.benefit.replaceFirstChar { it.uppercase() }

        holder.binding.tvId.text = item.id
        holder.binding.tvTitle.text = item.title
        holder.binding.tvDescription.text = formattedTitle
        holder.binding.tvBenefits.text = formattedBenefits

        val index = abs(position.hashCode()) % colorsMulti.size
        val colorRes = colorsMulti[index]

        val indexs = abs(position.hashCode()) % colorsMultiText.size
        val colorRess = colorsMultiText[indexs]

        holder.binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, colorRess))
        holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
    }
}