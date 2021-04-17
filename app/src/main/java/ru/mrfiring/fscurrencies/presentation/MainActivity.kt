package ru.mrfiring.fscurrencies.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import ru.mrfiring.fscurrencies.R
import ru.mrfiring.fscurrencies.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val adapter = CurrenciesRecyclerViewAdapter(viewModel::onItemClick)

        binding.currencyList.adapter = adapter

        viewModel.container.observe(this) {
            viewModel.viewModelScope.launch {
                it?.let {
                    viewModel.updateOldData(it)
                    adapter.submitList(it.currencies)
                }
            }
        }

        viewModel.selectedCurrency.observe(this) {
            val text = binding.leftCurrencyValue.text.toString()
            if (text.isEmpty()) {
                return@observe
            }
            updateRight(text, it.charCode, it.getValuePerNominal())
        }

        //Update calculations when text in left edit changes
        binding.leftCurrencyValue.addTextChangedListener { editable ->
            val text = editable.toString()
            if (text.isBlank()) {
                return@addTextChangedListener
            }
            viewModel.selectedCurrency.value?.let {
                updateRight(text, it.charCode, it.getValuePerNominal())
            }
        }

        //FAB onClick
        binding.fabRefresh.setOnClickListener {
            viewModel.onUpdateData()
        }

        //Loading state observe
        viewModel.status.observe(this) {
            when (it) {
                LoadingStatus.LOADING -> {
                    binding.currencyList.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.fabRefresh.visibility = View.GONE
                }
                LoadingStatus.DONE -> {
                    binding.currencyList.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.fabRefresh.visibility = View.VISIBLE
                }
                LoadingStatus.ERROR -> {
                    binding.fabRefresh.visibility = View.VISIBLE
                    if(adapter.itemCount > 0) {
                        binding.currencyList.visibility = View.VISIBLE
                    }
                    binding.progressBar.visibility = View.GONE
                    showErrorSnack()
                }
            }
        }

        setContentView(binding.root)
    }


    fun updateRight(value: String, charCode: String, valuePerNominal: Double) {
        val leftCurrent = value.toInt()

        binding.rightCurrencyName.text = charCode
        binding.rightCurrencyValue.setText(
            (leftCurrent / valuePerNominal).toString()
        )
    }

    private fun showErrorSnack() {
        val snackBar = Snackbar.make(
            binding.root,
            R.string.no_network,
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

}