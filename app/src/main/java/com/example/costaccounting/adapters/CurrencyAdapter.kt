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
import com.example.costaccounting.data.USDExchangeRate
import android.content.Intent
import android.app.Activity

class CurrencyAdapter(val context: Context): RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var currencyList = emptyList<String>()

    class CurrencyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyAdapter.CurrencyViewHolder {
        return CurrencyAdapter.CurrencyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.currency_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencyAdapter.CurrencyViewHolder, position: Int) {
        val code = currencyList[position]
        holder.itemView.findViewById<TextView>(R.id.textViewCurrencyCode).text = code
        val res = context.resources
        val codeId = res.getIdentifier(code, "string", context.packageName)
        holder.itemView.findViewById<TextView>(R.id.textViewCurrencyName).text = res.getString(codeId)
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

    fun setData(usdExchangeRate: List<String>) {
        currencyList = usdExchangeRate
        notifyDataSetChanged()
    }

}