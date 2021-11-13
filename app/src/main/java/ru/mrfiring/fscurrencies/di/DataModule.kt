package ru.mrfiring.fscurrencies.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mrfiring.fscurrencies.domain.CurrencyRepository
import ru.mrfiring.fscurrencies.data.CurrencyRepositoryImpl
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface DataModule {

    @Singleton
    @Binds
    fun provideCurrencyRepository(
        currencyRepositoryImpl: CurrencyRepositoryImpl
    ): CurrencyRepository
}