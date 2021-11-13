package ru.mrfiring.fscurrencies.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.mrfiring.fscurrencies.data.asDatabaseObject
import ru.mrfiring.fscurrencies.data.asDomainObject
import ru.mrfiring.fscurrencies.data.database.DatabaseContainerWithCurrencies
import ru.mrfiring.fscurrencies.data.datasouce.CurrenciesLocalDataSource
import ru.mrfiring.fscurrencies.data.datasouce.CurrenciesRemoteDataSource
import ru.mrfiring.fscurrencies.domain.CurrenciesRepository
import ru.mrfiring.fscurrencies.domain.DomainContainerWithCurrencies
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(
    private val currenciesLocalDataSource: CurrenciesLocalDataSource,
    private val currenciesRemoteDataSource: CurrenciesRemoteDataSource,
) : CurrenciesRepository {

    override suspend fun get(useCache: Boolean): Flow<List<DomainContainerWithCurrencies>> =
        withContext(Dispatchers.IO) {
            if (isCacheAvailable(useCache)) {
                getDataFromCache()
            } else {
                getDataFromNetwork()
            }
                .map { list -> list.map { it.asDomainObject() } }
        }

    private fun isCacheAvailable(useCache: Boolean) =
        useCache && !currenciesLocalDataSource.isCacheEmpty()

    private fun getDataFromCache() = currenciesLocalDataSource.get()

    private suspend fun getDataFromNetwork(): Flow<List<DatabaseContainerWithCurrencies>> {
        val remoteContainer = currenciesRemoteDataSource.get()
        val dbContainer = remoteContainer.asDatabaseObject(0)
        val dbCurrencies = remoteContainer.valute.values.map { it.asDatabaseObject(0) }

        currenciesLocalDataSource.clearCache()

        currenciesLocalDataSource.set(
            DatabaseContainerWithCurrencies(dbContainer, dbCurrencies)
        )

        return currenciesLocalDataSource.get()
    }
}