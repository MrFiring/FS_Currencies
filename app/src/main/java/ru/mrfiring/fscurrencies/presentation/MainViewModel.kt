package ru.mrfiring.fscurrencies.presentation

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.mrfiring.fscurrencies.domain.DomainCurrency
import ru.mrfiring.fscurrencies.domain.usecase.CalculateCurrencyValueUseCase
import ru.mrfiring.fscurrencies.domain.usecase.GetCurrenciesContainerUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrenciesContainerUseCase: GetCurrenciesContainerUseCase,
    private val calculateCurrencyValueUseCase: CalculateCurrencyValueUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<MainScreenState>(MainScreenState.Initial)
    val state: LiveData<MainScreenState> = _state

    init {
        loadData()
    }

    private fun loadData(fromCache: Boolean = true) {
        _state.value = MainScreenState.Loading

        viewModelScope.launch {
            try {
                getCurrenciesContainerUseCase(fromCache).collect {
                    setContentState(it.first().currencies)
                }
            } catch (throwable: Throwable) {
                _state.value = MainScreenState.Error(throwable)
            }
        }
    }

    private fun setContentState(currencies: List<DomainCurrency>) {
        _state.ensureContent(
            doIf = { content ->
                _state.value = content.copy(currenciesList = currencies)
            },
            doElse = {
                _state.value = MainScreenState.Content(currenciesList = currencies)
            }
        )
    }

    fun updateData() = loadData(fromCache = false)

    fun changeSource(text: String) {
        val newValue = text.toDouble()
        _state.ensureContent(
            doIf = { screenState ->
                val oldValue = screenState.leftCurrency.value
                if (oldValue == newValue) return@ensureContent

                val curSourceCurrency = screenState.leftCurrency.currency

                val newDestinationValue = calculateCurrencyValueUseCase(
                    sourceValue = newValue,
                    valuePerNominal = curSourceCurrency?.getValuePerNominal() ?: 0.0
                )

                val newLeftCurrency = screenState.leftCurrency.copy(value = newDestinationValue)
                val newRightCurrency = screenState.rightCurrency.copy(value = newValue)
                _state.value = screenState.copy(leftCurrency = newLeftCurrency, rightCurrency = newRightCurrency)
            }
        )
    }

    fun changeDestination(text: String) {
        val newValue = text.toDouble()
        _state.ensureContent(
            doIf = { screenState ->
                val oldValue = screenState.rightCurrency.value
                if (oldValue == newValue) return@ensureContent

                val newRightCurrency = screenState.rightCurrency.copy(value = newValue)
                _state.value = screenState.copy(rightCurrency = newRightCurrency)
            }
        )
    }

    fun itemSelected(item: DomainCurrency) {
        _state.ensureContent(doIf = { screenState ->
            _state.value = if (screenState.leftCurrency.currency == null) {
                val currencyItem = screenState.leftCurrency.copy(currency = item)
                screenState.copy(leftCurrency = currencyItem)
            } else {
                val currencyItem = screenState.rightCurrency.copy(currency = item)
                screenState.copy(rightCurrency = currencyItem)
            }
        })
    }

    private inline fun LiveData<MainScreenState>.ensureContent(
        crossinline doIf: (MainScreenState.Content) -> Unit,
        crossinline doElse: () -> Unit = {},
    ) {
        value?.let { screenState ->
            if (screenState is MainScreenState.Content) {
                doIf(screenState)
            } else {
                doElse()
            }
        }
    }
}