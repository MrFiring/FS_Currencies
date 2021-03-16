package ru.mrfiring.fscurrencies.data

import ru.mrfiring.fscurrencies.data.database.DatabaseContainer
import ru.mrfiring.fscurrencies.data.database.DatabaseContainerWithCurrencies
import ru.mrfiring.fscurrencies.data.database.DatabaseCurrency
import ru.mrfiring.fscurrencies.data.network.CurrenciesContainer
import ru.mrfiring.fscurrencies.data.network.Currency
import ru.mrfiring.fscurrencies.domain.DomainContainerWithCurrencies
import ru.mrfiring.fscurrencies.domain.DomainCurrency


fun Currency.asDatabaseObject(ownerId: Long): DatabaseCurrency{
    return DatabaseCurrency(id, ownerId ,numCode, charCode, nominal, name, value, previousValue)
}

fun DatabaseCurrency.asDomainObject(): DomainCurrency{
    return DomainCurrency(id, numCode, charCode, nominal, name, value, previousValue)
}


fun CurrenciesContainer.asDatabaseObject(id: Long): DatabaseContainer {
    return DatabaseContainer(id, date, previousDate, previousUrl)
}

fun DatabaseContainerWithCurrencies.asDomainObject(): DomainContainerWithCurrencies{
    return DomainContainerWithCurrencies(
        container.id,
        container.date,
        container.previousDate,
        container.previousUrl,
        currencies.map {
            it.asDomainObject()
        }
    )
}