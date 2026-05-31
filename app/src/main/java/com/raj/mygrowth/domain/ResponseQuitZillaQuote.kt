package com.raj.mygrowth.domain

data class ResponseQuitZillaQuote(val data: List<QuitZillaQuoteItem>)

data class QuitZillaQuoteItem(
    val Author: String,
    val Quote: String
)
