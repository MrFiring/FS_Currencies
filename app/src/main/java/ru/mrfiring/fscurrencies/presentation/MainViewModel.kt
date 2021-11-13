package ru.mrfiring.fscurrencies.presentation

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.mrfiring.fscurrencies.domain.DomainContainerWithCurrencies
import ru.mrfiring.fscurrencies.domain.DomainCurrency
import ru.mrfiring.fscurrencies.domain.FetchCurrenciesUseCase
import ru.mrfiring.fscurrencies.domain.GetContainerWithCurrenciesLiveDataUseCase
import ru.mrfiring.fscurrencies.domain.usecase.GetCurrenciesContainerUseCase
import java.io.IOException
import javax.inject.Inject

enum class LoadingStatus {
    LOADING, ERROR, DONE
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrenciesContainerUseCase: GetCurrenciesContainerUseCase,
    private val getContainerWithCurrenciesLiveDataUseCase: GetContainerWithCurrenciesLiveDataUseCase,
    private val fetchCurrenciesUseCase: FetchCurrenciesUseCase
) : ViewModel() {

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

    private fun fetchData(fromCache: Boolean = true, lastUpdate: String = "") = viewModelScope.launch {
        try {
            _status.value = LoadingStatus.LOADING
            fetchCurrenciesUseCase(fromCache, lastUpdate)
            _status.value = LoadingStatus.DONE

        } catch (ex: IOException) {
            _status.value = LoadingStatus.ERROR
            ex.printStackTrace()
        }
    }

    @Deprecated("Must be on the data layer")
    fun updateOldData(
        containerWithCurrencies: DomainContainerWithCurrencies
    ) = viewModelScope.launch {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        val date = formatter.parseDateTime(containerWithCurrencies.date)

        if(date.plusDays(1).isBeforeNow){
            fetchData(false, DateTime.now().toString(formatter))
        }

    }


    fun onUpdateData() = fetchData(false)

    fun onItemClick(item: DomainCurrency) {
        _selectedCurrency.value = item
    }
}