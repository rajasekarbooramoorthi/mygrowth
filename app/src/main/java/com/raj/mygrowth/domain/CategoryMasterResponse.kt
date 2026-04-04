package com.raj.mygrowth.domain


data class CategoryMasterResponse(
    val status: Boolean, val data: List<Category>
)

data class Category(
    val categoryId: Int,
    val categoryName: String,
    val categoryTag: String,
    val subcategoryList: List<SubCategory>
)

data class SubCategory(
    val subCategoryId: Int,
    val subcategoryName: String,
    val subCategoryTag: String,
    val itemList: List<Item>
)

data class Item(
    val id: String,
    val name: String,
    val itemTag: String,
    val links: List<String>
)