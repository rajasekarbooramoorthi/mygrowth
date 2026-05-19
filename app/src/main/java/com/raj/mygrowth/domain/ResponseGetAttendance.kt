package com.raj.mygrowth.domain

data class ResponseGetAttendance(
    val status: Boolean,
    val data: List<AttendanceItem>
)