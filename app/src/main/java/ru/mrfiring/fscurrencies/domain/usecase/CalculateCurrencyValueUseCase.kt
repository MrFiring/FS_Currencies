package ru.mrfiring.fscurrencies.domain.usecase

import javax.inject.Inject

class CalculateCurrencyValueUseCase @Inject constructor() {

    operator fun invoke(sourceValue: Double, valuePerNominal: Double): Double =
        sourceValue / valuePerNominal
}