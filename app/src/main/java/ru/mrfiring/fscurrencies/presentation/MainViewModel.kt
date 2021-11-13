package ru.mrfiring.fscurrencies.presentation

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.mrfiring.fscurrencies.domain.DomainCurrency
import ru.mrfiring.fscurrencies.domain.usecase.GetCurrenciesContainerUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrenciesContainerUseCase: GetCurrenciesContainerUseCase,
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
        _state.value?.let { screenState ->
            _state.value = if (screenState is MainScreenState.Content) {
                screenState.copy(currenciesList = currencies)
            } else {
                MainScreenState.Content(null, currencies)
            }
        }
    }

    fun onUpdateData() = loadData(fromCache = false)

    fun onItemClick(item: DomainCurrency) {
        _state.value?.let { screenState ->
            if (screenState is MainScreenState.Content) {
                _state.value = screenState.copy(rightCurrency = item)
            }
        }
    }
}