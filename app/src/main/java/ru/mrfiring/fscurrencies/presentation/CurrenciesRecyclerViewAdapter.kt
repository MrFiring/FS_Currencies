package ru.mrfiring.fscurrencies.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mrfiring.fscurrencies.R
import ru.mrfiring.fscurrencies.databinding.MainListItemBinding
import ru.mrfiring.fscurrencies.domain.DomainCurrency

class CurrenciesRecyclerViewAdapter(
    private val onClickListener: (DomainCurrency) -> Unit
) : ListAdapter<DomainCurrency, CurrenciesRecyclerViewAdapter.CurrencyViewHolder>(
    CurrencyDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    class CurrencyViewHolder(
        private val binding: MainListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainCurrency, clickListener: (DomainCurrency) -> Unit) {
            binding.apply {
                val context = root.context

                currencyCard.setOnClickListener { clickListener(item) }
                currencyName.text = item.name
                currencyShortName.text = item.charCode

                currentPrice.text = context.getString(
                    R.string.price_format,
                    item.value
                )
                val dPrice = item.getDeltaPrice()

                if(dPrice > 0){
                    deltaPrice.setTextColor(Color.GREEN)
                }else{
                    deltaPrice.setTextColor(Color.RED)
                }

                deltaPrice.text = context.getString(
                    R.string.price_delta_format,
                    item.getDeltaPrice()
                )
            }
        }

        companion object {
            fun from(parent: ViewGroup): CurrencyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MainListItemBinding.inflate(layoutInflater, parent, false)

                return CurrencyViewHolder(binding)
            }
        }
    }

    class CurrencyDiffCallback : DiffUtil.ItemCallback<DomainCurrency>() {
        override fun areItemsTheSame(oldItem: DomainCurrency, newItem: DomainCurrency): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DomainCurrency, newItem: DomainCurrency): Boolean {
            return oldItem == newItem
        }
    }
}