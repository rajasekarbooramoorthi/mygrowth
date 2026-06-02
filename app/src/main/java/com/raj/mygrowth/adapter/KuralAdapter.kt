package com.raj.mygrowth.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filter.FilterResults
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.ThirukuralAdapterBinding
import com.raj.mygrowth.domain.kural


class KuralAdapter(
    private val originalList: List<kural> = listOf()
) : RecyclerView.Adapter<KuralAdapter.ViewHolder>(), Filterable {

    private var filteredList = originalList.toMutableList()

    class ViewHolder(val binding: ThirukuralAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ThirukuralAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<kural>) {
        filteredList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(
        holder: ViewHolder, @SuppressLint("RecyclerView") position: Int
    ) {
        val item = filteredList[position]
        bind(holder.binding, item)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim() ?: ""

                val result = if (query.isEmpty()) {
                    originalList
                } else {
                    originalList.filter {
                        it.Line1.lowercase().contains(query) ||
                                it.Line2.lowercase().contains(query)
                    }
                }

                return FilterResults().apply {
                    values = result
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList.clear()
                filteredList.addAll(results?.values as List<kural>)
                notifyDataSetChanged()
            }
        }
    }

    private fun bind(binding: ThirukuralAdapterBinding, item: kural) {
        binding.apply {
            // tvAdhikaram.text = "${item.kural_no}.${item.pal_tamil} "
            // tvIyal.text = item.iyal_tamil
            tvKuralTamil.text = buildString {
                append(item.Line1)
                append("\n")
                append(item.Line2)
            }
            tvExplanationTamil.text = item.mk
            tvExplanationEnglish.text = item.explanation
        }
    }
}
