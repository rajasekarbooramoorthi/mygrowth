package com.raj.mygrowth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.ColorUtilities.colorsMulti
import com.raj.mygrowth.ColorUtilities.colorsMultiText
import com.raj.mygrowth.databinding.AdapterQuitZillaReportBinding
import com.raj.mygrowth.domain.ResponseQuitZillaMasterItem
import com.raj.mygrowth.interfaces.AdapterClick
import kotlin.math.abs

class QuitZillaReportAdapter(
    private val list: List<ResponseQuitZillaMasterItem>,
    private val context: Context,
    private val clickListener: AdapterClick,
) : RecyclerView.Adapter<QuitZillaReportAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterQuitZillaReportBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterQuitZillaReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        val formattedTitle = item.name.replaceFirstChar { it.uppercase() }

        holder.binding.tvName.text = formattedTitle
        holder.binding.tvDetail.text = "Day " + (item.days)
        holder.binding.tvDescription.text = item.description
        holder.binding.tvId.text = item.startDate + " vs " + item.endDate
        holder.binding.progressBar.progress = item.percentage
        holder.binding.tvProgress.text = item.percentage.toString() + "%"
        holder.binding.viewLine.setBackgroundColor(
            ContextCompat.getColor(
                context,
                colorsMultiText[abs(position.hashCode()) % colorsMultiText.size]
            )
        )
        holder.binding.cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                colorsMulti[abs(position.hashCode()) % colorsMulti.size]
            )
        )

        holder.binding.progressBar.setOnClickListener {
            clickListener.click(item.sno)
        }
    }
}