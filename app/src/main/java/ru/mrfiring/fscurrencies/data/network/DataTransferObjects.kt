package ru.mrfiring.fscurrencies.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrenciesContainer(
    @Json(name = "Date") val date: String,
    @Json(name = "PreviousDate") val previousDate: String,
    @Json(name = "PreviousURL") val previousUrl: String,
    @Json(name = "Timestamp") val timestamp: String,
    @Json(name = "Valute") val valute: Map<String, Currency>
)

@JsonClass(generateAdapter = true)
data class Currency(
    @Json(name = "ID") val id: String,
    @Json(name = "NumCode") val numCode: String,
    @Json(name = "CharCode") val charCode: String,
    @Json(name = "Nominal") val nominal: Int,
    @Json(name = "Name") val name: String,
    @Json(name = "Value") val value: Double,
    @Json(name = "Previous") val previousValue: Double
)