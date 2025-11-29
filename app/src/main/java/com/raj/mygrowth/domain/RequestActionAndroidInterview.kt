package com.raj.mygrowth.domain

data class RequestActionAndroidInterview(
    val action: String,
    val conceptModel: ArrayList<ConceptModel>?
)

data class ConceptModel(
    val conceptName: String?,
    val conceptId: String?,
    val links: List<String>?
)
