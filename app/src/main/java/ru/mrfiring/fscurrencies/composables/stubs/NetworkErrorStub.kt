package ru.mrfiring.fscurrencies.composables.stubs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.mrfiring.fscurrencies.R

@Composable
fun NetworkErrorStub(onRetryClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.no_network))
        OutlinedButton(onClick = onRetryClick) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}
