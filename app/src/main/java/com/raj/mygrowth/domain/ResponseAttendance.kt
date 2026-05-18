package com.raj.mygrowth.domain

data class ResponseAttendance(
    val status: Boolean,
    val data: List<AttendanceData>
)

data class AttendanceData(
    val date: String,
    val list: List<AttendanceItem>
)


data class AttendanceItem(
    val name: String,
    val id: String,
    val priority: String,
    val status: Int,
    val date: String,
)