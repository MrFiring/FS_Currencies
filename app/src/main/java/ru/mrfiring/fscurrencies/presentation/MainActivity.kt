package ru.mrfiring.fscurrencies.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import ru.mrfiring.fscurrencies.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        val adapter = CurrenciesRecyclerViewAdapter(viewModel::onItemClick)

        binding.currencyList.adapter = adapter

        viewModel.container.observe(this, Observer {
            if(it != null){
                adapter.submitList(it.currencies)
            }
        })

        setContentView(binding.root)
    }
}