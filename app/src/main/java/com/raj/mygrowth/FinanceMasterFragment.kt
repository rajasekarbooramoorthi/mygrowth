package com.raj.mygrowth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlin.math.roundToInt

class FinanceMasterFragment : Fragment(), FinancialClick {

    private var _binding: FragmentFinanceMasterBinding? = null
    private val binding get() = _binding!!

    private var totalPrinciple = 0.0
    private var totalInterest = 0.0

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                val response = api.getFinanceMasterData(
                    RequestAction("get_loan_details_with_summary")
                )

                binding.progressBar.visibility = View.GONE

                if (response.status) {

                    val financeList = getData(response.data)

                    with(binding) {
                        recyclerViewVertical.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = FinanceDetailsAdapter(financeList, this@FinanceMasterFragment)
                            isNestedScrollingEnabled = false
                        }

                        tvPrinciple.text = formatIndianCurrency(totalPrinciple.toInt())
                        tvInterest.text = formatIndianCurrency(totalInterest.toInt())
                        tvSumOfLoan.text = formatIndianCurrency((totalPrinciple + totalInterest).toInt())
                        tvAmountText.text = numberToIndianWords((totalPrinciple + totalInterest).toLong())
                        cardview.visibility = View.VISIBLE
                    }
                }

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), e.message ?: "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun click(id: String) {
        dialog(emptyList())
    }

    fun dialog(list: List<String>) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        dialog.setContentView(R.layout.bottom_dialog_update_loan)

        dialog.setOnShowListener { d ->
            val bottomSheet = (d as BottomSheetDialog)
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                BottomSheetBehavior.from(it).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        dialog.show()
    }

    /** ------------------------- CALCULATION FIXED & OPTIMIZED ------------------------- **/
    fun calculateTotalInterest(
        loanAmount: Double,
        interestPercentage: Double,
        loanOnDateStr: String
    ): LoanInterestResult {

        val loanDate = sdf.parse(loanOnDateStr)
            ?: throw IllegalArgumentException("Invalid date format: $loanOnDateStr")

        val start = Calendar.getInstance().apply { time = loanDate }
        val end = Calendar.getInstance()

        val diffMillis = end.timeInMillis - start.timeInMillis
        val totalDays = diffMillis / (1000 * 60 * 60 * 24)

        // yearly simple interest
        val interest = (loanAmount * interestPercentage * totalDays) / (100 * 365)
        val roundedInterest = (interest * 100).roundToInt() / 100.0
        val totalAmount = loanAmount + roundedInterest

        // YEARS - MONTHS - DAYS accurate calculation
        val temp = start.clone() as Calendar
        var years = 0
        var months = 0
        var days = 0

        while (temp.before(end)) {
            temp.add(Calendar.YEAR, 1)
            if (temp.after(end)) {
                temp.add(Calendar.YEAR, -1)
                break
            }
            years++
        }

        while (temp.before(end)) {
            temp.add(Calendar.MONTH, 1)
            if (temp.after(end)) {
                temp.add(Calendar.MONTH, -1)
                break
            }
            months++
        }

        days = ((end.timeInMillis - temp.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()

        val durationText = buildString {
            if (years > 0) append("$years year${if (years > 1) "s " else " "}")
            if (months > 0) append("$months month${if (months > 1) "s " else " "}")
            append("$days day${if (days != 1) "s" else ""}")
        }.trim()

        return LoanInterestResult(
            loanAmount = loanAmount,
            totalAmount = totalAmount,
            interestAmount = roundedInterest,
            numberOfDays = totalDays,
            durationText = durationText
        )
    }

    /** ------------------------- LOOP OPTIMIZED ------------------------- **/
    fun getData(data: ArrayList<ResponseFinanceItem>): ArrayList<FinanceData> {
        totalPrinciple = 0.0
        totalInterest = 0.0

        return data.map { item ->

            val remaining = (item.flmAmount - item.flmAmountPaid)

            val result = calculateTotalInterest(
                loanAmount = remaining,
                interestPercentage = item.flmInterest.toDouble(),
                loanOnDateStr = item.flmLoanDate
            )

            totalPrinciple += result.loanAmount
            totalInterest += result.interestAmount

            FinanceData(
                sno = item.flmSno,
                name = item.flmName,
                amountPrinciple = item.flmAmount.toInt(),
                description = item.flmDescription,
                interestAmount = result.interestAmount.toInt(),
                amountTotal = result.totalAmount.toInt(),
                days = result.numberOfDays.toInt(),
                dateOn = item.flmLoanDate,
                priority = item.flmPriority,
                reason = item.flmReason,
                percentage = item.flmInterest,
                status = item.flmState,
                durationText = result.durationText,
                daysRedable = result.durationText,
                flmAmountPaid = item.flmAmountPaid.toInt(),
                remining = remaining.toInt()
            )

        } as ArrayList<FinanceData>
    }

    /** ------------------------- NUMBER TO INDIAN WORDS ------------------------- **/
    fun numberToIndianWords(number: Long): String {
        if (number == 0L) return "Zero"

        val units = arrayOf(
            "", "one", "two", "three", "four", "five", "six", "seven",
            "eight", "nine", "ten", "eleven", "twelve", "thirteen",
            "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
        )

        val tens = arrayOf(
            "", "", "twenty", "thirty", "forty", "fifty",
            "sixty", "seventy", "eighty", "ninety"
        )

        fun twoDigits(n: Int) =
            if (n < 20) units[n] else tens[n / 10] + if (n % 10 != 0) " ${units[n % 10]}" else ""

        fun threeDigits(n: Int) =
            if (n < 100) twoDigits(n) else units[n / 100] + " hundred" +
                    if (n % 100 != 0) " ${twoDigits(n % 100)}" else ""

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

        if (num > 0) result.append(threeDigits(num.toInt()))

        return result.toString().trim().replaceFirstChar { it.uppercase() }
    }

    /** ------------------------- INDIAN CURRENCY FORMAT ------------------------- **/
    private val indianCurrencyFormatter by lazy {
        val symbols = DecimalFormatSymbols(Locale("en", "IN")).apply {
            currencySymbol = "₹"
        }
        DecimalFormat("##,##,###", symbols)
    }

    fun formatIndianCurrency(amount: Number): String {
        return indianCurrencyFormatter.format(amount.toLong())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class LoanInterestResult(
        val loanAmount: Double,
        val totalAmount: Double,
        val interestAmount: Double,
        val numberOfDays: Long,
        val durationText: String
    )
}
