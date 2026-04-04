package com.raj.mygrowth.interfaces

import com.raj.mygrowth.domain.Item
import com.raj.mygrowth.domain.SubCategory


interface MasterInterFace {
    fun clickSubCategory(list: List<SubCategory>)
    fun clickItem(list: List<Item>)
}