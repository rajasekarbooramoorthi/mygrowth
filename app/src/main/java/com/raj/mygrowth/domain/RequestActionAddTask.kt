package com.raj.mygrowth.domain

data class RequestActionAddTask(
    val action: String,
    val taskName: String,
    val description: String,
    val dueDate: String,
    val priority: String
)

