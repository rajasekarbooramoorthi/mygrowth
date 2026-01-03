package com.raj.mygrowth.domain

data class ResponseFinance(
    val status: Boolean,
    val data: ArrayList<ResponseFinanceItem>
)

data class ResponseFinanceItem(
    val flmSno: Int,
    val flmName: String,
    val flmAmount: Double,
    val flmAmountPaid: Double,
    val flmInterest: Int,
    val flmState: String,
    val flmLoanDate: String,
    val flmDays: String,
    val flmDescription: String,
    val flmReason: String,
    val flmPriority: String,
)


data class FinanceData(
    val sno: Int,
    val name: String,
    val description: String,
    val amountTotal: Int,
    val amountPrinciple: Int,
    val interestAmount: Int,
    val percentage: Int,
    val reason: String,
    val durationText: String,
    val status: String,
    val priority: String,
    val days: Int,
    val flmAmountPaid: Int,
    val daysRedable: String,
    val dateOn: String,
)

data class calculateInterestByDays(
    val loanAmount: Double,
    val interestPercentage: Double,
    val numberOfDays: Long
)