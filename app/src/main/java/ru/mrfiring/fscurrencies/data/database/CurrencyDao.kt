package ru.mrfiring.fscurrencies.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao{

    @Transaction
    @Query("select * from databasecontainer")
    fun getContainersWithCurrencies(): Flow<List<DatabaseContainerWithCurrencies>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContainer(item: DatabaseContainer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCurrencies(items: List<DatabaseCurrency>)

    @Query("select count(*) from databasecontainer")
    fun getContainersCount(): Int

    @Query("delete from databasecontainer")
    fun deleteAllContainers()

    @Query("delete from databasecurrency")
    fun deleteAllCurrencies()

}