package ru.mrfiring.fscurrencies.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mrfiring.fscurrencies.domain.CurrencyRepository
import ru.mrfiring.fscurrencies.domain.CurrencyRepositoryImpl
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCurrencyRepository(
        currencyRepositoryImpl: CurrencyRepositoryImpl
    ): CurrencyRepository = currencyRepositoryImpl

}