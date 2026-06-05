package com.raj.mygrowth.domain

class RequestDankDetailsUpdate(
    val action: String,
    val id: String,
    val name: String,
    val cust_id: String,
    val acc_number: String,
    val card_num: String,
    val cpin: String,
    val psw: String,
    val profile_password: String,
    val user_name: String,
    val ifsc_code: String,
)