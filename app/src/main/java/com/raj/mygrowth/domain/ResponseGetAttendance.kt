package com.raj.mygrowth.domain

data class ResponseGetAttendance(
    val status: Boolean,
    val data: List<AttendanceGetItem>
)

data class AttendanceGetItem(
    val name: String,
    val id: String,
    val priority: Int,
)