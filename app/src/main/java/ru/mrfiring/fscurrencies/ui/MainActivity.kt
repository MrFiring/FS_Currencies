package ru.mrfiring.fscurrencies.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.fscurrencies.R
import ru.mrfiring.fscurrencies.composables.CurrencyInputsCard
import ru.mrfiring.fscurrencies.composables.CurrencyList
import ru.mrfiring.fscurrencies.composables.stubs.FullScreenCircularLoading
import ru.mrfiring.fscurrencies.composables.stubs.NetworkErrorStub
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
                Text(text = stringResource(id = R.string.no_data_available))
            }
        }

        MainScreenState.Loading -> {
            FullScreenCircularLoading()
        }

        is MainScreenState.Content -> {
            MainScreenContent(state as MainScreenState.Content, viewModel)
        }

        is MainScreenState.Error -> {
            NetworkErrorStub(viewModel::updateData)
        }
    }
}

@Composable
fun MainScreenContent(state: MainScreenState.Content, viewModel: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        CurrencyInputsCard(
            sourceCurrencyItem = state.leftCurrency,
            onSourceChange = viewModel::changeDestination,
            destinationCurrencyItem = state.rightCurrency,
            onDestinationChange = viewModel::changeSource,
        )
        CurrencyList(currencies = state.currenciesList, onItemClick = viewModel::itemSelected)
    }
}