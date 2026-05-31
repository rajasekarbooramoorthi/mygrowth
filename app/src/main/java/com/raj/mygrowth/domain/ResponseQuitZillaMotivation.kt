package com.raj.mygrowth.domain

data class ResponseQuitZillaMotivation(val data: List<ResponseQuitZillaMotivationItem>)

data class ResponseQuitZillaMotivationItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String
)
