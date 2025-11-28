package com.raj.mygrowth.domain

data class DailyTaskResponse(
    val status: Boolean, val message: String?, val data: ArrayList<DailyTask>
)

data class DailyTask(
    val taskName: String,
    val list: ArrayList<DailyTaskItem>
)

data class DailyTaskItem(
    val dt_sno: String,
    val dt_id: String,
    val dt_name: String,
    val dt_status: String,
    val dt_date_created: String,
    val dt_date_updated: String,
    val dt_due_date: String,
    val tag: String
)