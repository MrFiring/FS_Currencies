package ru.mrfiring.fscurrencies.domain

data class DomainContainerWithCurrencies(
    val id: Long,
    val date: String,
    val previousDate: String,
    val previousUrl: String,
    val currencies: List<DomainCurrency>
)

data class DomainCurrency(
    val id: String,
    val numCode: String,
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value: Double,
    val previousValue: Double
) {
    fun getDeltaPrice(): Double = previousValue - value
    fun getValuePerNominal(): Double = value / nominal
}