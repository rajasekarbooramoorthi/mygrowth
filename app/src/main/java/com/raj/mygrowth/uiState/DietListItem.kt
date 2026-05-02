package com.raj.mygrowth.uiState

import com.raj.mygrowth.domain.DietItem

sealed class DietListItem {

    data class Header(
        val title: String
    ) : DietListItem()

    data class Meal(
        val item: DietItem
    ) : DietListItem()

    data class Rule(
        val text: String
    ) : DietListItem()
}