package com.raj.mygrowth.domain

data class DietResponse(
    val dailyTarget: DailyTarget,
    val dietPlan: List<DietItem>,
    val rules: List<String>
)

data class DailyTarget(
    val calories: String,
    val protein: String,
    val mealFrequency: String
)

data class DietItem(
    val time: String,
    val label: String? = null,
    val foods: List<String>,
    val note: String? = null
)