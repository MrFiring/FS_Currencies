package ru.mrfiring.fscurrencies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mrfiring.fscurrencies.data.database.CurrencyDao
import ru.mrfiring.fscurrencies.data.network.CurrenciesService
import ru.mrfiring.fscurrencies.domain.CurrencyRepository
import ru.mrfiring.fscurrencies.domain.DomainContainerWithCurrencies
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val service: CurrenciesService
): CurrencyRepository {
    override fun getContainerWithCurrenciesLiveData(): LiveData<DomainContainerWithCurrencies>{
        return Transformations.map(currencyDao.getContainersWithCurrencies()){
            it.map { container ->
                container.asDomainObject()
            }.first()
        }
    }

    override suspend fun fetchCurrencies(fromCache: Boolean) {
        withContext(Dispatchers.IO){
            if(!fromCache || currencyDao.getContainersCount() == 0) {
                val networkContainer = service.getCurrenciesContainer()
                currencyDao.deleteAllContainers()
                currencyDao.deleteAllCurrencies()

                currencyDao.insertContainer(networkContainer.asDatabaseObject(0))

                currencyDao.insertAllCurrencies(
                    ArrayList(networkContainer.valute.values).map {
                        it.asDatabaseObject(0)
                    }
                )
            }
        }
    }
}