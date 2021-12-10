package ru.mrfiring.fscurrencies.presentation

import ru.mrfiring.fscurrencies.domain.DomainCurrency

data class CurrencyItem(
    val currency: DomainCurrency? = null,
    val value: String = "0"
)

