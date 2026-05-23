package com.raj.mygrowth.domain


data class WeightGainResponse(
    val currentWeight: List<WeightGainItem>,
    val targetWeight: List<WeightGainItem>
)

data class WeightGainItem(
    val week: String,
    val weight: Float
)
