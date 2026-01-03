package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raj.mygrowth.databinding.FragmentFinanceMasterBinding
import com.raj.mygrowth.domain.FinanceData
import com.raj.mygrowth.domain.RequestAction
import com.raj.mygrowth.domain.ResponseFinanceItem
import com.raj.mygrowth.interfaces.FinancialClick
import com.raj.mygrowth.networkUtility.ApiService
import com.raj.mygrowth.networkUtility.RetrofitClient
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FinanceMasterFragment : Fragment(), FinancialClick {

    private var _binding: FragmentFinanceMasterBinding? = null
    private val binding get() = _binding!!

    var totalPrinciple = 0
    var totalInterest = 0
    var totalSumOfLoan: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinanceMasterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDetails()
    }


    private fun loadDetails() {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response =
                    api.getFinanceMasterData(RequestAction("get_loan_details_with_summary"))

                binding.progressBar.visibility = View.GONE

                if (response.status) {

                    binding.recyclerViewVertical.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    val adapter =
                        FinanceDetailsAdapter(getData(response.data), this@FinanceMasterFragment)
                    binding.recyclerViewVertical.adapter = adapter
                    binding.recyclerViewVertical.isNestedScrollingEnabled = false
                    val totalPrincipalInWords = numberToIndianWords(totalSumOfLoan)

                    binding.tvAmountText.text = totalPrincipalInWords
                    binding.tvInterest.text = formatIndianCurrency(totalInterest)
                    binding.tvPrinciple.text = formatIndianCurrency(totalPrinciple)
                    binding.tvSumOfLoan.text = formatIndianCurrency(totalSumOfLoan.toInt())

                    binding.cardview.visibility = View.VISIBLE

                }

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun click(id: String) {

        dialog(ArrayList<String>())
    }


    fun dialog(list: List<String>) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        dialog.setContentView(R.layout.bottom_dialog_update_loan)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet
                )

            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true

                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }

       // val rvDialog = dialog.findViewById<RecyclerView>(R.id.rvDialog)
        //rvDialog?.layoutManager = LinearLayoutManager(requireContext())
        //rvDialog?.adapter = DialogAdapterGenericAdapter(list, this@FinanceMasterFragment)
       // rvDialog?.isNestedScrollingEnabled = true

        dialog.show()
    }


    fun numberToIndianWords(number: Long): String {
        if (number == 0L) return "zero"

        val units = arrayOf(
            "", "one", "two", "three", "four", "five",
            "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen",
            "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
        )

        val tens = arrayOf(
            "", "", "twenty", "thirty", "forty",
            "fifty", "sixty", "seventy", "eighty", "ninety"
        )

        fun twoDigits(n: Int): String =
            when {
                n < 20 -> units[n]
                else -> tens[n / 10] + if (n % 10 != 0) " ${units[n % 10]}" else ""
            }

        fun threeDigits(n: Int): String =
            when {
                n < 100 -> twoDigits(n)
                else -> units[n / 100] + " hundred" +
                        if (n % 100 != 0) " ${twoDigits(n % 100)}" else ""
            }

        var num = number
        val result = StringBuilder()

        val crore = num / 10000000
        if (crore > 0) {
            result.append(threeDigits(crore.toInt())).append(" crore ")
            num %= 10000000
        }

        val lakh = num / 100000
        if (lakh > 0) {
            result.append(threeDigits(lakh.toInt())).append(" lakh ")
            num %= 100000
        }

        val thousand = num / 1000
        if (thousand > 0) {
            result.append(threeDigits(thousand.toInt())).append(" thousand ")
            num %= 1000
        }

        if (num > 0) {
            result.append(threeDigits(num.toInt()))
        }

        return result.toString().trim().replaceFirstChar { it.uppercase() }
    }

    fun getData(data: ArrayList<ResponseFinanceItem>): ArrayList<FinanceData> {
        val listArray = ArrayList<FinanceData>()
        totalPrinciple = 0
        totalInterest = 0
        totalSumOfLoan = 0
        for (i in 0 until data.size) {
            val dataItem = data[i]
            val cts = calculateTotalInterest(
                loanAmount = (dataItem.flmAmount - dataItem.flmAmountPaid),
                interestPercentage = dataItem.flmInterest.toDouble(),
                loanOnDateStr = dataItem.flmLoanDate
            )

            totalPrinciple += (dataItem.flmAmount.toInt() - dataItem.flmAmountPaid.toInt())
            totalInterest += cts.interestAmount.toInt()
            totalSumOfLoan += (cts.totalAmount.toInt() - dataItem.flmAmountPaid.toInt())

            val sd = FinanceData(
                sno = dataItem.flmSno,
                name = dataItem.flmName,
                amountPrinciple = dataItem.flmAmount.toInt(),
                description = dataItem.flmDescription,
                interestAmount = cts.interestAmount.toInt(),
                amountTotal = cts.totalAmount.toInt(),
                days = cts.numberOfDays.toInt(),
                dateOn = dataItem.flmLoanDate,
                priority = dataItem.flmPriority,
                reason = dataItem.flmReason,
                percentage = dataItem.flmInterest,
                status = dataItem.flmState,
                durationText = cts.durationText,
                daysRedable = cts.durationText,
                flmAmountPaid = dataItem.flmAmountPaid.toInt(),
                remining = (dataItem.flmAmount.toInt() - dataItem.flmAmountPaid.toInt())
            )
            listArray.add(sd)

        }

        return listArray
    }

    fun calculateTotalInterest(
        loanAmount: Double,
        interestPercentage: Double,
        loanOnDateStr: String
    ): LoanInterestResult {

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val loanDate = sdf.parse(loanOnDateStr)
            ?: throw IllegalArgumentException("Invalid date format")

        val start = Calendar.getInstance().apply { time = loanDate }
        val end = Calendar.getInstance()

        // --- Calculate Years, Months, Days ---
        var years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR)
        var months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH)
        var days = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH)

        if (days < 0) {
            months--
            val temp = start.clone() as Calendar
            temp.add(Calendar.MONTH, 1)
            temp.add(Calendar.DAY_OF_MONTH, -1)
            days += temp.get(Calendar.DAY_OF_MONTH)
        }

        if (months < 0) {
            years--
            months += 12
        }

        // --- Total Days (for interest calculation) ---
        val diffInMillis = end.timeInMillis - start.timeInMillis
        val totalDays = diffInMillis / (1000 * 60 * 60 * 24)

        // --- Interest Calculation ---
        val interest =
            (loanAmount * interestPercentage * totalDays) / (100 * 365)

        val roundedInterest = Math.round(interest * 100) / 100.0
        val totalAmount = Math.round((loanAmount + roundedInterest) * 100) / 100.0

        // --- Duration Text ---
        val durationText = buildString {
            if (years > 0) append("$years year${if (years > 1) "s" else ""} ")
            if (months > 0) append("$months month${if (months > 1) "s" else ""} ")
            if (days > 0 || length == 0)
                append("$days day${if (days != 1) "s" else ""}")
        }.trim()

        return LoanInterestResult(
            totalAmount = totalAmount,
            interestAmount = roundedInterest,
            numberOfDays = totalDays,
            durationText = durationText
        )
    }


    data class LoanInterestResult(
        val totalAmount: Double,
        val interestAmount: Double,
        val numberOfDays: Long,
        val durationText: String
    )

    fun formatIndianCurrency(amount: Int): String {
        val symbols = DecimalFormatSymbols(Locale("en", "IN")).apply {
            currencySymbol = "₹"
        }

        val formatter = DecimalFormat("#,##,##0", symbols)
        return formatter.format(amount)
    }
}
