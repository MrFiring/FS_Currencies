package ru.mrfiring.fscurrencies.domain

import androidx.lifecycle.LiveData

interface CurrencyRepository {

    fun getContainerWithCurrenciesLiveData(): LiveData<DomainContainerWithCurrencies?>

    suspend fun fetchCurrencies(fromCache: Boolean)
}