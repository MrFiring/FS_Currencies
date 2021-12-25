package ru.mrfiring.fscurrencies.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import ru.mrfiring.fscurrencies.R
import ru.mrfiring.fscurrencies.presentation.CurrencyItem

@Composable
fun CurrencyInput(
    item: CurrencyItem,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    val empty = item.currency == null
    var focused by remember { mutableStateOf(false) }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = item.currency?.name ?: stringResource(R.string.select_currency))
        OutlinedTextField(
            value = item.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (focused) {
                    onValueChange(it)
                }
            },
            enabled = !empty, modifier = Modifier.onFocusChanged {
                focused = it.isFocused
            })
    }
}