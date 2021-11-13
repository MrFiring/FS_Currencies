package ru.mrfiring.fscurrencies.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mrfiring.fscurrencies.data.datasouce.CurrenciesLocalDataSource
import ru.mrfiring.fscurrencies.data.datasouce.CurrenciesLocalDataSourceImpl
import ru.mrfiring.fscurrencies.data.datasouce.CurrenciesRemoteDataSource
import ru.mrfiring.fscurrencies.data.datasouce.CurrenciesRemoteDataSourceImpl
import ru.mrfiring.fscurrencies.data.repository.CurrenciesRepositoryImpl
import ru.mrfiring.fscurrencies.domain.CurrenciesRepository
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface DataModule {

    @Singleton
    @Binds
    fun bindCurrenciesLocalDataSource(
        dataSource: CurrenciesLocalDataSourceImpl
    ): CurrenciesLocalDataSource

    @Singleton
    @Binds
    fun bindCurrenciesRemoteDataSource(
        dataSource: CurrenciesRemoteDataSourceImpl
    ): CurrenciesRemoteDataSource

    @Singleton
    @Binds
    fun bindCurrenciesRepository(
        repositoryImpl: CurrenciesRepositoryImpl
    ): CurrenciesRepository
}