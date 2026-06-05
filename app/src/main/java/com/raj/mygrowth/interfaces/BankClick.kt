package com.raj.mygrowth.interfaces

import com.raj.mygrowth.domain.BankItem

interface BankClick {
    fun clickBankDetails(request: BankItem)
}