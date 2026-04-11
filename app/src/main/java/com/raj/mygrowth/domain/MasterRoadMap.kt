package com.raj.mygrowth.domain


data class MasterRoadmap(
    val data: List<Phase>
)

data class Phase(
    val phase: String,
    val categories: List<CategoryMR>
)

data class CategoryMR(
    val name: String,
    val subcategories: List<SubCategoryMR>
)

data class SubCategoryMR(
    val technology: String,
    val topics: List<String>
)