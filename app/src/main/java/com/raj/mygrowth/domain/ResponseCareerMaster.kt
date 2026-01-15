package com.raj.mygrowth.domain

data class ResponseCareerMaster(
    val status: Boolean,
    val message: String?,
    val data: ArrayList<CareerMaster>
)

data class CareerMaster(
    val sno: String,
    val id: String,
    val name: String,
    val tag: String,
    val due: String,
    val type: String,
    val status: String
)