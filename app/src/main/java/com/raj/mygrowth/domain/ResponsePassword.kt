package com.raj.mygrowth.domain

data class ResponsePassword(
    val status: Boolean, val message: String?, val data: ArrayList<PasswordItem>
)

data class PasswordItem(
    val psw_sno: String,
    val psw_name: String,
    val psw_password: String,
    val psw_date_updated: String
)

