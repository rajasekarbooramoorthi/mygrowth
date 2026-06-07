package com.raj.mygrowth.domain

data class ResponseBankDetails(
    val status: Boolean, val message: String?, val data: ArrayList<BankItem>
)
data class BankItem(
    val id: String,
    val bank_name: String,
    val customer_id: String,
    val ifsc_code: String,
    val user_name: String,
    val account_number: String,
    val card_number: String,
    val card_pin: String,
    val login_password: String,
    val profile_password: String,
    val updated_date: String,
    val days: String
)