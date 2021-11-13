package ru.mrfiring.fscurrencies.data.datasouce

import androidx.lifecycle.LiveData
import ru.mrfiring.fscurrencies.data.database.CurrencyDao
import ru.mrfiring.fscurrencies.data.database.DatabaseContainerWithCurrencies
import javax.inject.Inject

interface CurrenciesLocalDataSource {

    fun get(): LiveData<List<DatabaseContainerWithCurrencies>>

    fun clearCache()
}

class CurrenciesLocalDataSourceImpl @Inject constructor(
    private val database: CurrencyDao,
) : CurrenciesLocalDataSource {
    override fun get(): LiveData<List<DatabaseContainerWithCurrencies>> =
        database.getContainersWithCurrencies()

    override fun clearCache() = database.run {
        deleteAllContainers()
        deleteAllCurrencies()
    }
}