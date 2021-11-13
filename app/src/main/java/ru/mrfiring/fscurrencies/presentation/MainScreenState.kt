package ru.mrfiring.fscurrencies.presentation

import ru.mrfiring.fscurrencies.domain.DomainCurrency

sealed class MainScreenState {

    object Initial : MainScreenState()

    object Loading : MainScreenState()

    data class Content(
        val rightCurrency: DomainCurrency?,
        val currenciesList: List<DomainCurrency>,
    ) : MainScreenState()

    data class Error(val throwable: Throwable) : MainScreenState()
}
