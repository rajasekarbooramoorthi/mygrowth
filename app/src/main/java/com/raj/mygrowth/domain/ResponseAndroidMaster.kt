package com.raj.mygrowth.domain

data class ResponseAndroidMaster(
    val status: Boolean, val message: String?, val data: ArrayList<AndroidMaster>
)

data class AndroidMaster(
    val ms_sno: String,
    val ms_id: String,
    val ms_name: String,
    val ms_tag: String,
    val links: ArrayList<String>?
)

