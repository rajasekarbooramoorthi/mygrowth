package com.raj.mygrowth.domain

data class DailyTaskResponse(
    val status: Boolean, val message: String?, val data: Data
)

data class Data(
    val today: ArrayList<DailyTask>,
    val tomorrow: ArrayList<DailyTask>,
    val this_week: ArrayList<DailyTask>,
    val this_month: ArrayList<DailyTask>,
    val next_month: ArrayList<DailyTask>,
    val future: ArrayList<DailyTask>,
    val overdue: ArrayList<DailyTask>,
    val all_list: ArrayList<DailyTask>

)

data class DailyTask(
    val dt_sno: String,
    val dt_id: String,
    val dt_name: String,
    val dt_status: String,
    val dt_date_created: String,
    val dt_date_updated: String,
    val dt_due_date: String,
    val tag: String
)