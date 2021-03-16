package ru.mrfiring.fscurrencies.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import ru.mrfiring.fscurrencies.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val adapter = CurrenciesRecyclerViewAdapter(viewModel::onItemClick)

        binding.currencyList.adapter = adapter

        viewModel.container.observe(this) {
            viewModel.viewModelScope.launch {
                it?.let {
                    adapter.submitList(it.currencies)
                }
            }
        }

        viewModel.selectedCurrency.observe(this){
            val text = binding.leftCurrencyValue.text.toString()
            if(text.isEmpty()) {
                return@observe
            }
            updateRight(text, it.charCode, it.getValuePerNominal())
        }

        binding.leftCurrencyValue.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if(text.isBlank()){
                    return
                }
                viewModel.selectedCurrency.value?.let {
                    updateRight(text, it.charCode, it.getValuePerNominal())
                }

            }
        })


        setContentView(binding.root)
    }


    fun updateRight(value: String, charCode: String, valuePerNominal: Double){
        val leftCurrent = value.toInt()

        binding.rightCurrencyName.text = charCode
        binding.rightCurrencyValue.setText(
            (valuePerNominal * leftCurrent).toString()
        )
    }
}