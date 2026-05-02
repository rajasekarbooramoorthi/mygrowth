package com.raj.mygrowth.domain

data class ResponseAttendance(
    val status: Boolean,
    val data: List<AttendanceItem>
)

data class AttendanceItem(
    val status: Boolean,
    val name: String,
    val priority: String,
    val date: String
)

