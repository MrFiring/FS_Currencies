package ru.mrfiring.fscurrencies.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetContainerWithCurrenciesLiveDataUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    operator fun invoke(): LiveData<DomainContainerWithCurrencies?> {
        return currencyRepository.getContainerWithCurrenciesLiveData()
    }
}