package com.raj.mygrowth.interfaces

interface SimpleClick {
    fun click(id: String, path: String, type: String)
    fun clickChild(list: List<String>)
    fun clickUrl(url: String)

}