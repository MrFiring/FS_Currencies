package ru.mrfiring.fscurrencies.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.fscurrencies.R
import ru.mrfiring.fscurrencies.databinding.ActivityMainBinding
import ru.mrfiring.fscurrencies.presentation.CurrenciesRecyclerViewAdapter
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

        adapter = CurrenciesRecyclerViewAdapter(viewModel::itemSelected)
        binding.currencyList.adapter = adapter

        setupListeners()
        observeState()

        setContentView(binding.root)
    }

    private fun setupListeners() = binding.run {
        //FAB onClick
        fabRefresh.setOnClickListener {
            viewModel.updateData()
        }

        //source currency
        leftCurrencyValue.addTextChangedListener { editable ->
            val text = editable.toString()

            if (leftCurrencyValue.hasFocus() && text.isNotBlank()) {
                viewModel.changeDestination(editable.toString())
            }
        }

        //destination currency
        rightCurrencyValue.addTextChangedListener { editable ->
            val text = editable.toString()
            if (rightCurrencyValue.hasFocus() && text.isNotBlank()) {
                viewModel.changeSource(text)
            }
        }
    }

    private fun observeState() {
        viewModel.state.observe(this, ::applyState)
    }

    private fun applyState(state: MainScreenState) =
        when (state) {
            MainScreenState.Initial -> Unit

            MainScreenState.Loading -> binding.run {
                currencyList.isVisible = false
                progressBar.isVisible = true
                fabRefresh.isVisible = false
            }

            is MainScreenState.Content -> {
                applyContent(state)
            }

            is MainScreenState.Error -> binding.run {
                fabRefresh.isVisible = true
                progressBar.isVisible = false
                showError()
            }
        }

    private fun applyContent(state: MainScreenState.Content) = binding.run {
        currencyList.isVisible = true
        progressBar.isVisible = false
        fabRefresh.isVisible = true

        adapter?.submitList(state.currenciesList)

        state.leftCurrency.currency?.let { currency ->
            leftCurrencyName.text = currency.charCode
            leftCurrencyValue.isEnabled = true
        }

        if (!leftCurrencyValue.hasFocus()) {
            leftCurrencyValue.setText(state.leftCurrency.value.toString())
        }

        state.rightCurrency.currency?.let { currency ->
            rightCurrencyName.text = currency.charCode
            rightCurrencyValue.isEnabled = true
        }

        if (!rightCurrencyValue.hasFocus()) {
            rightCurrencyValue.setText(state.rightCurrency.value.toString())
        }

        val currencyInputEnabled =
            state.rightCurrency.currency != null && state.leftCurrency.currency != null

        leftCurrencyValue.isEnabled = currencyInputEnabled
        rightCurrencyValue.isEnabled = currencyInputEnabled
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