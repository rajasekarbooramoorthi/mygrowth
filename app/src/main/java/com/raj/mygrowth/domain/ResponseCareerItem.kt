package com.raj.mygrowth.domain

data class ResponseCareerItem(
    val status: Boolean,
    val message: String?,
    val data: ArrayList<CareerItem>
)

data class CareerItem(
    val sno: String,
    val id: String,
    val name: String,
    val description: String,
    val link: String,
    val status: String
)