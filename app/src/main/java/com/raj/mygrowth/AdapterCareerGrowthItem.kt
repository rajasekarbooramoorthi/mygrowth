package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemCareerBinding
import com.raj.mygrowth.domain.CareerItem
import com.raj.mygrowth.interfaces.SimpleClick

class AdapterCareerGrowthItem(
    private val list: ArrayList<CareerItem>, click_: SimpleClick
) : RecyclerView.Adapter<AdapterCareerGrowthItem.ViewHolder>() {
    var click = click_

    class ViewHolder(val binding: AdapterItemCareerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemCareerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.item = item
        holder.binding.tvName.text =
            HtmlCompat.fromHtml(item.id + "\t\t" + item.name, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.binding.root.setOnClickListener {
           // item.link?.let { click.clickChild(it) }

        }
        holder.binding.executePendingBindings()
    }
}
