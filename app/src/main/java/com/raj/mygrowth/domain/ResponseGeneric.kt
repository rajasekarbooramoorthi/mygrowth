package com.raj.mygrowth.domain

data class ResponseGeneric(
    val status: Boolean,
    val message: String?,
    val data: ArrayList<ResponseGenericItem>
)

data class ResponseGenericItem(
    val id: String,
    val name: String,
    val tag: String,
    val folder: String="",
    val type: String="",

    val links: ArrayList<String>?
)