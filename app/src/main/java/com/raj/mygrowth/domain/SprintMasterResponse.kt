package com.raj.mygrowth.domain

data class SprintMasterResponse(val data: List<SprintMasterItem>)

data class SprintMasterItem(
    val id: String,
    val name: String,
    val description: String,
    val status: String,
    val sdate: String,
    val edate: String
)
