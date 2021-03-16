package ru.mrfiring.fscurrencies.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mrfiring.fscurrencies.domain.DomainContainerWithCurrencies
import ru.mrfiring.fscurrencies.domain.DomainCurrency
import ru.mrfiring.fscurrencies.domain.FetchCurrenciesUseCase
import ru.mrfiring.fscurrencies.domain.GetContainerWithCurrenciesLiveDataUseCase
import java.io.IOException
import javax.inject.Inject

enum class LoadingStatus{
    LOADING, ERROR, DONE
}

class MainViewModel @Inject constructor(
    application: Application,
    private val getContainerWithCurrenciesLiveDataUseCase: GetContainerWithCurrenciesLiveDataUseCase,
    private val fetchCurrenciesUseCase: FetchCurrenciesUseCase
): AndroidViewModel(application) {

    val container: LiveData<DomainContainerWithCurrencies?> =
        getContainerWithCurrenciesLiveDataUseCase()

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
    get() = _status

    private val _selectedCurrency = MutableLiveData<DomainCurrency>()
    val selectedCurrency: LiveData<DomainCurrency>
    get() = _selectedCurrency


    init {
        fetchData()
    }

    private fun fetchData(fromCache: Boolean = true) = viewModelScope.launch {
        try {
            _status.value = LoadingStatus.LOADING
            fetchCurrenciesUseCase(fromCache)
            _status.value = LoadingStatus.DONE

        }catch (ex: IOException){
            ex.printStackTrace()
        }
    }

    fun onUpdateData() = fetchData(false)

    fun onItemClick(item: DomainCurrency) {
        _selectedCurrency.value = item
    }
}