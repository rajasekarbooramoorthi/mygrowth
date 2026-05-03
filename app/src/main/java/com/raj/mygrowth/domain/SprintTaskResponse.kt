package com.raj.mygrowth.domain

data class SprintTaskResponse(val data: List<SprintTaskItem>)

data class SprintTaskItem(
    val id: String,
    val name: String,
    val description: String,
    val status: String,
    val date: String
)
