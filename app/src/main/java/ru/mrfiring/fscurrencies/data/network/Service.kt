package ru.mrfiring.fscurrencies.data.network

import retrofit2.http.GET

const val BASE_URL = "https://www.cbr-xml-daily.ru/"

interface CurrenciesService{
    @GET("daily_json.js")
    suspend fun getCurrenciesList(): CurrenciesContainer
}