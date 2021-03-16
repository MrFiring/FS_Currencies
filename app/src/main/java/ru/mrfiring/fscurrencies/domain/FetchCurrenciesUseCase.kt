package ru.mrfiring.fscurrencies.domain

import javax.inject.Inject

class FetchCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(fromCache: Boolean){
        currencyRepository.fetchCurrencies(fromCache)
    }
}