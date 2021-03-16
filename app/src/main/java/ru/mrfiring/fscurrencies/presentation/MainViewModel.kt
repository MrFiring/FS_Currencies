package ru.mrfiring.fscurrencies.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat
import ru.mrfiring.fscurrencies.domain.DomainContainerWithCurrencies
import ru.mrfiring.fscurrencies.domain.DomainCurrency
import ru.mrfiring.fscurrencies.domain.FetchCurrenciesUseCase
import ru.mrfiring.fscurrencies.domain.GetContainerWithCurrenciesLiveDataUseCase
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

enum class LoadingStatus {
    LOADING, ERROR, DONE
}

class MainViewModel @Inject constructor(
    application: Application,
    private val getContainerWithCurrenciesLiveDataUseCase: GetContainerWithCurrenciesLiveDataUseCase,
    private val fetchCurrenciesUseCase: FetchCurrenciesUseCase
) : AndroidViewModel(application) {

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

        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun updateOldData(
        containerWithCurrencies: DomainContainerWithCurrencies
    ) = viewModelScope.launch {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        val date = formatter.parseDateTime(containerWithCurrencies.date)

        if(date.plusDays(1).isBeforeNow){
            fetchData(false)
        }

    }


    fun onUpdateData() = fetchData(false)

    fun onItemClick(item: DomainCurrency) {
        _selectedCurrency.value = item
    }
}