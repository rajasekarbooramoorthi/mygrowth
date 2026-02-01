package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterItemMyGrowthActionviewBinding
import com.raj.mygrowth.domain.ResponseGenericItem
import com.raj.mygrowth.interfaces.SimpleClick
import android.widget.Filter
import android.widget.Filterable
class MygrowthitemactionClickAdapter(
    private val originalList: List<ResponseGenericItem>,
    click_: SimpleClick,
    path_: String
) : RecyclerView.Adapter<MygrowthitemactionClickAdapter.ViewHolder>(), Filterable {

    private var filteredList = originalList.toMutableList()

    var click = click_
    var path = path_

    class ViewHolder(val binding: AdapterItemMyGrowthActionviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemMyGrowthActionviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]

        holder.binding.item = item
        holder.binding.tvName.text =
            HtmlCompat.fromHtml(item.id + "\t\t" + item.name, HtmlCompat.FROM_HTML_MODE_LEGACY)

        holder.binding.root.setOnClickListener {
            item.links?.let { click.callIntent(it, path) }
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
                filteredList.addAll(results?.values as List<ResponseGenericItem>)
                notifyDataSetChanged()
            }
        }
    }
}
