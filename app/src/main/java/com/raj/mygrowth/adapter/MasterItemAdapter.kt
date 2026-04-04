package com.raj.mygrowth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.MasterItemAdapterBinding
import com.raj.mygrowth.domain.Item
import com.raj.mygrowth.interfaces.MasterInterFace


class MasterItemAdapter(
    private val originalList: List<Item>,
    click_: MasterInterFace,
    folderName_: String,
    fileType_: String
) : RecyclerView.Adapter<MasterItemAdapter.ViewHolder>(), Filterable {

    private var filteredList = originalList.toMutableList()

    var click = click_
    var folderName = folderName_
    var fileType = fileType_

    class ViewHolder(val binding: MasterItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MasterItemAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]

        //holder.binding.tvName.text = item.name
        holder.binding.tvName.text = HtmlCompat.fromHtml(
            item.id + "\t\t" + item.name, HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        holder.binding.root.setOnClickListener {
            item.links?.let { click.callIntent(it, folderName, fileType) }
        }

        holder.binding.executePendingBindings()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim() ?: ""

                val result = if (query.isEmpty()) {
                    originalList
                } else {
                    originalList.filter {
                        it.name.lowercase().contains(query) || it.id.contains(query)
                    }
                }

                return FilterResults().apply {
                    values = result
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList.clear()
                filteredList.addAll(results?.values as List<Item>)
                notifyDataSetChanged()
            }
        }
    }
}