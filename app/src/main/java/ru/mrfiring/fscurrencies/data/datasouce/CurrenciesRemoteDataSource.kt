package ru.mrfiring.fscurrencies.data.datasouce

import ru.mrfiring.fscurrencies.data.network.CurrenciesContainer
import ru.mrfiring.fscurrencies.data.network.CurrenciesService
import javax.inject.Inject

interface CurrenciesRemoteDataSource {

    suspend fun get(): CurrenciesContainer
}

class CurrenciesRemoteDataSourceImpl @Inject constructor(
    private val service: CurrenciesService,
) : CurrenciesRemoteDataSource {

    override suspend fun get(): CurrenciesContainer = service.get()
}