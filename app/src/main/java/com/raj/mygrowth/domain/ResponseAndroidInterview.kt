package com.raj.mygrowth.domain

data class ResponseAndroidInterview(
    val status: Boolean, val message: String?, val data: ArrayList<AndroidInterviewItem>
)

data class AndroidInterviewItem(
    val id: String,
    val name: String,
    val links: ArrayList<String>
)

