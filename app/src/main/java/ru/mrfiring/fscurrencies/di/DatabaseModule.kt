package ru.mrfiring.fscurrencies.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.mrfiring.fscurrencies.data.database.CurrencyDao
import ru.mrfiring.fscurrencies.data.database.CurrencyDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CurrencyDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            CurrencyDatabase::class.java,
            "currency_db"
        ).build()

    @Singleton
    @Provides
    fun provideCurrencyDao(currencyDatabase: CurrencyDatabase): CurrencyDao =
        currencyDatabase.currencyDao
}