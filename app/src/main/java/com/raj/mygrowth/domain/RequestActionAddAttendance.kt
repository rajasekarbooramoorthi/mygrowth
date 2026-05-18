package com.raj.mygrowth.domain

data class RequestActionAddAttendance(
    val action: String,
    val status: Int,
    val id: String,
    val date: String
)

