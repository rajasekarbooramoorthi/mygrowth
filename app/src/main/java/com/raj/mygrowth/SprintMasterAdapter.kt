package com.raj.mygrowth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterMasterSprintBinding
import com.raj.mygrowth.domain.SprintMasterItem
import com.raj.mygrowth.interfaces.AdapterClick

class SprintMasterAdapter(
    private val list: List<SprintMasterItem>,
    private val context: Context,
    private val click_: AdapterClick,
    ) : RecyclerView.Adapter<SprintMasterAdapter.ViewHolder>() {

    val click = click_
    private var selectedPosition = 0

    class ViewHolder(val binding: AdapterMasterSprintBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMasterSprintBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = list[position]

        val formattedTitle = item.name.replaceFirstChar { it.uppercase() }

        holder.binding.tvName.text = formattedTitle
        holder.binding.tvDescription.text = item.description

        //isDateBetween(getCurrentDate(),item.sdate,item.edate)

        if (position == selectedPosition) {
            holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.green_dark_50))
        } else {
            holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.grey_dark_50))
        }

        holder.binding.parent.setOnClickListener {

            val previous = selectedPosition
            selectedPosition = position

            if (previous != RecyclerView.NO_POSITION) {
                notifyItemChanged(previous)
            }
            notifyItemChanged(selectedPosition)
            click.click(item.id)
        }

    }
}