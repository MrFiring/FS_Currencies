package ru.mrfiring.fscurrencies.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class DatabaseContainerWithCurrencies(
    @Embedded
    val container: DatabaseContainer,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val currencies: List<DatabaseCurrency>
)

@Entity
data class DatabaseContainer(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val date: String,
    val previousDate: String,
    val previousUrl: String,
)

@Entity
data class DatabaseCurrency(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val ownerId: Long,
    val numCode: String,
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value : Double,
    val previousValue: Double,
)