package ru.mrfiring.fscurrencies.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.fscurrencies.R
import ru.mrfiring.fscurrencies.composables.CurrencyInput
import ru.mrfiring.fscurrencies.composables.CurrencyList
import ru.mrfiring.fscurrencies.presentation.MainScreenState
import ru.mrfiring.fscurrencies.presentation.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()

    when (state) {
        MainScreenState.Initial -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Data will be here as soon as possible.")
            }
        }

        MainScreenState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is MainScreenState.Content -> {
            MainScreenContent(state as MainScreenState.Content, viewModel)
        }

        is MainScreenState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = """${stringResource(id = R.string.no_network)}
                    ${(state as MainScreenState.Error).throwable}""".trimMargin()
                )
                OutlinedButton(onClick = viewModel::updateData) {
                    Text(text = stringResource(id = R.string.retry))
                }
            }
        }
    }
}

@Composable
fun MainScreenContent(state: MainScreenState.Content, viewModel: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                CurrencyInput(
                    item = state.leftCurrency,
                    onValueChange = viewModel::changeDestination,
                    modifier = Modifier.weight(0.5f)
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                CurrencyInput(
                    item = state.rightCurrency,
                    onValueChange = viewModel::changeSource,
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
        CurrencyList(currencies = state.currenciesList, onItemClick = viewModel::itemSelected)
    }
}