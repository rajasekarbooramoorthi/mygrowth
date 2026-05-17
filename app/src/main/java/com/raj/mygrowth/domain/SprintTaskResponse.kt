package com.raj.mygrowth.domain

data class SprintTaskResponse(val data: List<SprintTaskItem>)

data class SprintTaskItem(
    val id: String,
    val name: String,
    val description: String,
    val details: String?,
    val status: String,
    val date: String? = ""
)
