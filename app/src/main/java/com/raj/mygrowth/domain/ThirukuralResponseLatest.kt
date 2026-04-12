package com.raj.mygrowth.domain

data class ThirukuralResponseLatest(
    val kural: List<kural>
)

data class kural(
    val Number: String,
    val Line1: String,
    val Line2: String,
    val Translation: String,
    val couplet: String,
    val sp: String,
    val mk: String,
    val mv: String,
    val explanation: String
)
