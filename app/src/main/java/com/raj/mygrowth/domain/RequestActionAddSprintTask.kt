package com.raj.mygrowth.domain

data class RequestActionAddSprintTask(
    val action: String,
    val name: String,
    val description: String,
    val details: String,
    val priority: String,
    val id: String
)

