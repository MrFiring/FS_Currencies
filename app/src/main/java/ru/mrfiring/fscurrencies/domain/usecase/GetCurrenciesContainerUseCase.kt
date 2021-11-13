package ru.mrfiring.fscurrencies.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mrfiring.fscurrencies.domain.CurrenciesRepository
import ru.mrfiring.fscurrencies.domain.DomainContainerWithCurrencies
import javax.inject.Inject

class GetCurrenciesContainerUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository,
) {

    suspend operator fun invoke(
        useCache: Boolean = true
    ): Flow<List<DomainContainerWithCurrencies>> =
        currenciesRepository.get(useCache)
}