package com.raj.mygrowth.domain

data class ResponseQuitZillaMaster(val data: List<ResponseQuitZillaMasterItem>)

data class ResponseQuitZillaMasterItem(
    val id: String,
    val name: String,
    val description: String,
    val status: String,
    val startDate: String,
    val endDate: String,
    val days: String
)
