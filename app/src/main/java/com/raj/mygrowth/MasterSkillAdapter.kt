package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemMasterSkillBinding
import com.raj.mygrowth.domain.AndroidMaster
import com.raj.mygrowth.interfaces.SimpleClick

class MasterSkillAdapter(
    private val list: List<AndroidMaster>,
    private val recyclerView: RecyclerView,
    click_: SimpleClick
) : RecyclerView.Adapter<MasterSkillAdapter.ViewHolder>() {
    var click = click_


    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(val binding: AdapterItemMasterSkillBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemMasterSkillBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size
    fun setClickListener() {
        click
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.item = item

        val card = holder.binding.card
        val context = holder.binding.root.context

        val selectedBg = ContextCompat.getColor(context, R.color.light_blue_50)
        val unSelectedBg = ContextCompat.getColor(context, android.R.color.white)

        val selectedStroke = ContextCompat.getColor(context, R.color.blue)
        val unSelectedStroke = ContextCompat.getColor(context, android.R.color.darker_gray)

        if (position == selectedPosition) {
            //card.setCardBackgroundColor(selectedBg)
            //card.strokeColor = selectedStroke
        } else {
            // card.setCardBackgroundColor(unSelectedBg)
            //card.strokeColor = unSelectedStroke
        }

        holder.binding.root.setOnClickListener {
            val prevPos = selectedPosition
            selectedPosition = holder.adapterPosition

            notifyItemChanged(prevPos)
            notifyItemChanged(selectedPosition)

            recyclerView.smoothScrollToPosition(selectedPosition)

            click.click(item.asm_id)
        }

        holder.binding.executePendingBindings()
    }
}
