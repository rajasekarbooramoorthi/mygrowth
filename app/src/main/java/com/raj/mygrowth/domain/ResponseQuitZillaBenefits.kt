package com.raj.mygrowth.domain

data class ResponseQuitZillaBenefits(val data: List<ResponseQuitZillaBenefitsItem>)

data class ResponseQuitZillaBenefitsItem(
    val id: String,
    val title: String,
    val description: String,
    val benefit: String
)
