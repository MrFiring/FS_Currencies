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

    fun changeSource(newValue: String) {
        _state.ensureContent(
            doIf = { screenState ->
                val oldValue = screenState.rightCurrency.value
                if (oldValue == newValue) return@ensureContent

                if (newValue.isEmpty()) {
                    val newRightCurrency = screenState.rightCurrency.copy(value = newValue)
                    _state.value = screenState.copy(
                        rightCurrency = newRightCurrency
                    )
                    return@ensureContent
                }

                val curSourceCurrency = screenState.leftCurrency.currency
                val curDestinationCurrency = screenState.rightCurrency.currency

                val newDestinationValue = curDestinationCurrency?.let { dest ->
                    curSourceCurrency?.let { src ->
                        calculateCurrencyValueUseCase(
                            sourceCount = newValue.toDouble(),
                            sourceCurrency = dest,
                            destinationCurrency = src
                        )
                    }
                } ?: throw IllegalStateException("Both currencies must be determined")

                val newLeftCurrency =
                    screenState.leftCurrency.copy(value = newDestinationValue.toString())
                val newRightCurrency = screenState.rightCurrency.copy(value = newValue)
                _state.value = screenState.copy(
                    leftCurrency = newLeftCurrency,
                    rightCurrency = newRightCurrency
                )
            }
        )
    }

    fun changeDestination(newValue: String) {
        _state.ensureContent(
            doIf = { screenState ->
                val oldValue = screenState.leftCurrency.value
                if (oldValue == newValue) return@ensureContent

                if (newValue.isEmpty()) {
                    val newLeftCurrency = screenState.leftCurrency.copy(value = newValue)
                    _state.value = screenState.copy(
                        leftCurrency = newLeftCurrency
                    )
                    return@ensureContent
                }

                val curSourceCurrency = screenState.leftCurrency.currency
                val curDestinationCurrency = screenState.rightCurrency.currency

                val newDestinationValue = curDestinationCurrency?.let { dest ->
                    curSourceCurrency?.let { src ->
                        calculateCurrencyValueUseCase(
                            sourceCount = newValue.toDouble(),
                            sourceCurrency = src,
                            destinationCurrency = dest
                        )
                    }
                } ?: throw IllegalStateException("Both currencies must be determined")

                val newRightCurrency =
                    screenState.rightCurrency.copy(value = newDestinationValue.toString())
                val newLeftCurrency = screenState.leftCurrency.copy(value = newValue)
                _state.value = screenState.copy(
                    leftCurrency = newLeftCurrency,
                    rightCurrency = newRightCurrency
                )
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