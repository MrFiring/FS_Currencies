package ru.mrfiring.fscurrencies.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.mrfiring.fscurrencies.data.network.BASE_URL
import ru.mrfiring.fscurrencies.data.network.CurrenciesService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideStockService(retrofit: Retrofit): CurrenciesService =
        retrofit.create(CurrenciesService::class.java)

}