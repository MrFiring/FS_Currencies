package ru.mrfiring.fscurrencies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import ru.mrfiring.fscurrencies.R
import ru.mrfiring.fscurrencies.databinding.ActivityMainBinding
import ru.mrfiring.fscurrencies.presentation.CurrenciesRecyclerViewAdapter
import ru.mrfiring.fscurrencies.presentation.LoadingStatus
import ru.mrfiring.fscurrencies.presentation.MainScreenState
import ru.mrfiring.fscurrencies.presentation.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private var adapter: CurrenciesRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        adapter = CurrenciesRecyclerViewAdapter(viewModel::onItemClick)
        binding.currencyList.adapter = adapter

        setupListeners()
        observeState()

        setContentView(binding.root)
    }

    private fun setupListeners() {
        //FAB onClick
        binding.fabRefresh.setOnClickListener {
            viewModel.onUpdateData()
        }
    }

    private fun observeState() {
        viewModel.state.observe(this, ::applyState)
    }

    private fun applyState(state: MainScreenState) =
        when (state) {
            MainScreenState.Initial -> Unit

            MainScreenState.Loading -> {
                binding.currencyList.isVisible = false
                binding.progressBar.isVisible = true
                binding.fabRefresh.isVisible = false
            }

            is MainScreenState.Content -> binding.run {
                currencyList.isVisible = true
                progressBar.isVisible = false
                fabRefresh.isVisible = true

                adapter?.submitList(state.currenciesList)
                state.rightCurrency?.let { currency ->
                    rightCurrencyName.text = currency.charCode
                }
            }

            is MainScreenState.Error -> {
                binding.fabRefresh.isVisible = true
                binding.progressBar.isVisible = false
                showError()
            }
        }

    private fun showError() {
        Snackbar
            .make(binding.root, R.string.no_network, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onDestroy() {
        binding.currencyList.adapter = null
        adapter = null
        super.onDestroy()
    }
}