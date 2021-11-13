package ru.mrfiring.fscurrencies.data.datasouce

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.mrfiring.fscurrencies.data.database.CurrencyDao
import ru.mrfiring.fscurrencies.data.database.DatabaseContainerWithCurrencies
import javax.inject.Inject

interface CurrenciesLocalDataSource {

    fun get(): Flow<List<DatabaseContainerWithCurrencies>>

    fun set(container: DatabaseContainerWithCurrencies)

    fun isCacheEmpty(): Boolean

    fun clearCache()
}

class CurrenciesLocalDataSourceImpl @Inject constructor(
    private val database: CurrencyDao,
) : CurrenciesLocalDataSource {
    override fun get(): Flow<List<DatabaseContainerWithCurrencies>> =
        database.getContainersWithCurrencies()

    override fun set(container: DatabaseContainerWithCurrencies) = database.run {
        insertContainer(container.container)
        insertAllCurrencies(container.currencies)
    }

    override fun isCacheEmpty(): Boolean =
        database.getContainersCount() == 0

    override fun clearCache() = database.run {
        deleteAllContainers()
        deleteAllCurrencies()
    }
}