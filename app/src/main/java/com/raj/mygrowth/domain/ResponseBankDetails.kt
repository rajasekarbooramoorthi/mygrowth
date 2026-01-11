package com.raj.mygrowth.domain

data class ResponseBankDetails(
    val status: Boolean, val message: String?, val data: ArrayList<BankItem>
)

data class BankItem(
    val b_sno: String,
    val b_name: String,
    val b_cust_id: String,
    val b_acc_number: String,
    val b_card_num: String,
    val b_cpin: String,
    val b_psw: String,
    val b_profile_password: String,
    val b_updated: String
)

