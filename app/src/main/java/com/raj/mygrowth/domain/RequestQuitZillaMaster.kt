package com.raj.mygrowth.domain

data class RequestQuitZillaMaster(
    val id: String,
    val action: String,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val status: String
)

