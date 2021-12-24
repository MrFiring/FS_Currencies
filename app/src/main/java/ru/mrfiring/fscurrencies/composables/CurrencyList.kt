package ru.mrfiring.fscurrencies.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.mrfiring.fscurrencies.R
import ru.mrfiring.fscurrencies.domain.DomainCurrency

@Composable
fun CurrencyList(currencies: List<DomainCurrency>, onItemClick: (DomainCurrency) -> Unit) {
    LazyColumn(content = {
        items(count = currencies.size) { index: Int ->
            CurrencyItem(item = currencies[index], onClick = { onItemClick(it) })
        }
    })
}

@Composable
internal fun CurrencyItem(item: DomainCurrency, onClick: (DomainCurrency) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .clickable { onClick(item) }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(all = 8.dp)
        ) {
            Column {
                Text(text = item.name, fontWeight = FontWeight.Bold)
                Text(text = item.charCode, fontWeight = FontWeight.Light)
            }
            Column {
                Text(text = stringResource(id = R.string.price_format, item.value))
                val delta = item.getDeltaPrice()
                Text(
                    text = stringResource(id = R.string.price_delta_format, delta),
                    color = if (delta > 0) Color.Green else Color.Red
                )
            }
        }
    }
}