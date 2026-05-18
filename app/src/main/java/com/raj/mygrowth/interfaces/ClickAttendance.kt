package com.raj.mygrowth.interfaces

import com.raj.mygrowth.domain.RequestActionAddAttendance

interface ClickAttendance {
    fun click(request: RequestActionAddAttendance)
}