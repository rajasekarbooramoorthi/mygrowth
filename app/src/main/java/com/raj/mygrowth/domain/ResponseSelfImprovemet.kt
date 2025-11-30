package com.raj.mygrowth.domain

data class ResponseSelfImprovement(
    val status: Boolean, val message: String?, val data: ArrayList<ResponseSelfImprovementItem>
)

data class ResponseSelfImprovementItem(
    val ai_sno: String,
    val ai_name: String,
    val ai_description: String,
    val ai_link: String
)

