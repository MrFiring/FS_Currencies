package ru.mrfiring.fscurrencies.domain.usecase

import ru.mrfiring.fscurrencies.domain.DomainCurrency
import javax.inject.Inject

class CalculateCurrencyValueUseCase @Inject constructor() {

    operator fun invoke(
        sourceCount: Double,
        sourceCurrency: DomainCurrency,
        destinationCurrency: DomainCurrency
    ): Double =
       sourceCount * sourceCurrency.getValuePerNominal() / destinationCurrency.getValuePerNominal()
}