package com.raj.mygrowth.interfaces

import com.raj.mygrowth.domain.Item
import com.raj.mygrowth.domain.SubCategory


interface MasterInterFace {
    fun clickSubCategory(list: List<SubCategory>)
    fun clickItem(list: List<Item>, folderName: String, filetype: String)

    fun callIntent(list: List<String>, folderName: String, filetype: String)

    fun loadUrl(url: String, path: String)
}