package com.raj.mygrowth.domain

data class ThirukuralResponse(
    val type: String,
    val features: List<Feature>
)

data class Feature(
    val properties: Properties
)

data class Properties(
    val id: Int,

    val pal_bamini: String,
    val pal_english: String,
    val pal_thanglish: String,
    val pal_tamil: String,
    val iyal_bamini: String,
    val iyal_english: String,
    val iyal_thanglish: String,
    val iyal_tamil: String,
    val adhikarm_no: Int,
    val adhikarm_bamini: String,
    val adhikarm_english: String,
    val adhikarm_thanglish: String,
    val adhikarm_tamil: String,
    val kural_no: Int,
    val kural_bamini1: String,
    val kural_bamini2: String,
    val kural_thanglish1: String,
    val kural_thanglish2: String,
    val kuralvilakam_tamil: String,
    val kuralvilakam_english: String,
    val kural_tamil1: String,
    val isfav: Int
)