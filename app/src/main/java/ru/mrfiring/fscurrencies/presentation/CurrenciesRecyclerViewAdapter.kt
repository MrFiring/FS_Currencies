package ru.mrfiring.fscurrencies.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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