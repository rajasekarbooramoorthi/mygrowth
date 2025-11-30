package com.raj.mygrowth.domain

data class ResponseAndroidMaster(
    val status: Boolean, val message: String?, val data: ArrayList<AndroidMaster>
)

data class AndroidMaster(
    val asm_sno: String,
    val asm_id: String,
    val asm_name: String,
    val asm_tag: String,
    val links: ArrayList<String>?
)

