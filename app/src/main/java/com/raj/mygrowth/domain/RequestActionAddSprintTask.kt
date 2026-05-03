package com.raj.mygrowth.domain

data class RequestActionAddSprintTask(
    val action: String,
    val taskName: String,
    val description: String,
    val details: String,
    val priority: String,
    val id: String
)

