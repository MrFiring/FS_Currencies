package ru.mrfiring.fscurrencies.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        Row {
            Column(modifier = Modifier.weight(0.7f)) {
                Text(text = item.charCode)
                Text(text = item.name)
            }
            Column(modifier = Modifier.weight(0.3f)) {
                Text(text = item.value.toString())
                val delta = item.getDeltaPrice()
                Text(text = delta.toString(), color = if (delta > 0) Color.Green else Color.Red)
            }
        }
    }
}