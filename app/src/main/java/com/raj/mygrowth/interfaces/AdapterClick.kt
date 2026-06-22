package com.raj.mygrowth.interfaces

import com.raj.mygrowth.domain.ResponseQuitZillaMasterItem

interface AdapterClick {
    fun click(id: String)
    fun clickDetails(id: String, item: ResponseQuitZillaMasterItem) {
//
    }
}