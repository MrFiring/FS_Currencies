package ru.mrfiring.fscurrencies.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mrfiring.fscurrencies.presentation.CurrencyItem

@Composable
fun CurrencyInputsCard(
    sourceCurrencyItem: CurrencyItem,
    onSourceChange: (String) -> Unit,
    destinationCurrencyItem: CurrencyItem,
    onDestinationChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(all = 8.dp)
        ) {
            CurrencyInput(
                item = sourceCurrencyItem,
                onValueChange = onSourceChange,
                modifier = Modifier.weight(0.5f)
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            CurrencyInput(
                item = destinationCurrencyItem,
                onValueChange = onDestinationChange,
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}
