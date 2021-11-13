package ru.mrfiring.fscurrencies.domain

import kotlinx.coroutines.flow.Flow

interface CurrenciesRepository {

    suspend fun get(useCache: Boolean): Flow<List<DomainContainerWithCurrencies>>
}