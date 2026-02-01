package com.raj.mygrowth.interfaces

interface SimpleClick {
    fun click(id: String, path: String, type: String)

    fun callApi(id: String) {
        //optional
    }

    fun actionID(action: String, id: String, path: String) {
        //optional
    }

    fun clickChild(list: List<String>)

    fun callIntent(list: List<String>, path: String) {
        //call Intent
    }
    fun clickUrl(url: String)
    fun loadUrl(url: String, path: String) {
        //optional
    }

    fun checkCompleted(id: String)

}