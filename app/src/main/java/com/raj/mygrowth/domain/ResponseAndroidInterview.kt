package com.raj.mygrowth.domain

data class ResponseAndroidInterview(
    val status: Boolean, val message: String?, val data: ArrayList<AndroidInterviewItem>
)

data class AndroidInterviewItem(
    val concept_id: String,
    val concept_name: String,
    val links: ArrayList<String>
)

