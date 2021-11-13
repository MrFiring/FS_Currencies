package ru.mrfiring.fscurrencies.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [
    DatabaseContainer::class,
    DatabaseCurrency::class
], version = 1)
abstract class CurrencyDatabase: RoomDatabase(){
    abstract val currencyDao: CurrencyDao
}