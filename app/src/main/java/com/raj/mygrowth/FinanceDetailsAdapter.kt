package com.raj.mygrowth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.databinding.AdapterFinanceDetailsBinding
import com.raj.mygrowth.domain.FinanceData
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class FinanceDetailsAdapter(
    private val list: ArrayList<FinanceData>
) : RecyclerView.Adapter<FinanceDetailsAdapter.ViewHolder>() {


    class ViewHolder(val binding: AdapterFinanceDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterFinanceDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.let { bind ->
            bind.tvName.text = item.name
            bind.tvDescription.text = item.description
            bind.tvAmount.text =
                formatIndianCurrency(item.amountPrinciple) + " %" + item.percentage
            bind.tvInterest.text = formatIndianCurrency(item.interestAmount)
            bind.tvTotalAmount.text = formatIndianCurrency(item.amountTotal)
            bind.tvDays.text = item.daysRedable
            bind.tvTotalAmountPaid.text = formatIndianCurrency(item.flmAmountPaid)
            bind.tvLoanOnDate.text = item.dateOn
            bind.tvPriority.text = item.priority
            bind.tvStatus.text = item.status
        }
    }

    fun formatIndianCurrency(amount: Int): String {
        val symbols = DecimalFormatSymbols(Locale("en", "IN")).apply {
            currencySymbol = "₹"
        }

        val formatter = DecimalFormat("#,##,##0", symbols)
        return formatter.format(amount)
    }
}
