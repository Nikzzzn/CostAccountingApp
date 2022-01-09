package com.example.costaccounting.adapters

import android.app.Activity.RESULT_OK
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.R
import android.content.Intent
import android.app.Activity
import android.util.Log
import android.widget.Filter
import android.widget.Filterable

class CurrencyAdapter(val context: Context): RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>(), Filterable {

    private var currencyList: MutableList<Currency> = mutableListOf()
    private var currencyListFull: List<Currency> = emptyList()

    private val currencyFilter: Filter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Currency> = mutableListOf()

            if(constraint == null || constraint.isEmpty()){
                filteredList.addAll(currencyListFull)
            } else{
                val filterPattern = constraint.toString().lowercase().trim()

                Log.d("asdf", filterPattern)

                for(currency: Currency in currencyListFull){
                    if(currency.name.lowercase().contains(filterPattern) ||
                            currency.code.lowercase().contains(filterPattern)){
                        filteredList.add(currency)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            currencyList.clear()
            currencyList.addAll(results?.values as MutableList<Currency>)
            notifyDataSetChanged()
        }
    }

    class CurrencyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class Currency(val name: String, val code: String)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.currency_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val code = currencyList[position].code
        val name = currencyList[position].name
        holder.itemView.findViewById<TextView>(R.id.textViewCurrencyCode).text = code
        holder.itemView.findViewById<TextView>(R.id.textViewCurrencyName).text = name
        holder.itemView.findViewById<ConstraintLayout>(R.id.currencyRow).setOnClickListener {
            val i = Intent()
            i.putExtra("currency", code)
            if (context is Activity) {
                context.setResult(RESULT_OK, i)
                context.finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    override fun getFilter(): Filter {
        return currencyFilter
    }

    fun setData(currencyCodes: List<String>) {
        val res = context.resources
        val namesList = currencyCodes.map {
            res.getString(res.getIdentifier(it, "string", context.packageName))
        }
        currencyList = namesList.zip(currencyCodes) { a, b ->
            Currency(a, b)
        }.toMutableList()
        currencyListFull = currencyList.toMutableList()

        notifyDataSetChanged()
    }

}