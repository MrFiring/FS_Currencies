package ru.mrfiring.fscurrencies.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.selects.select

@Dao
interface CurrencyDao{

    @Transaction
    @Query("select * from databasecontainer")
    fun getContainerWithCurrencies(): LiveData<DatabaseContainerWithCurrencies>

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

@Database(entities = [
    DatabaseContainer::class,
    DatabaseCurrency::class
], version = 1)
abstract class CurrencyDatabase: RoomDatabase(){
    abstract val currencyDao: CurrencyDao
}