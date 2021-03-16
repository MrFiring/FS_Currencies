package ru.mrfiring.fscurrencies.data

import ru.mrfiring.fscurrencies.data.database.DatabaseContainer
import ru.mrfiring.fscurrencies.data.database.DatabaseCurrency
import ru.mrfiring.fscurrencies.data.network.CurrenciesContainer
import ru.mrfiring.fscurrencies.data.network.Currency


fun Currency.asDatabaseObject(ownerId: Long): DatabaseCurrency{
    return DatabaseCurrency(id, ownerId ,numCode, charCode, nominal, name, value, previousValue)
}



fun CurrenciesContainer.asDatabaseContainer(id: Long): DatabaseContainer {
    return DatabaseContainer(id, date, previousDate, previousUrl)
}