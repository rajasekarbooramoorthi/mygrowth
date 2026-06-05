package com.raj.mygrowth.domain

data class RequestActionUpdatePassword(
    val action: String,
    val id: String,
    val password: String
)
