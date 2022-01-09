package com.example.costaccounting.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.R
import com.example.costaccounting.data.Account

class AccountsAdapter: RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    private var accountsList = emptyList<Account>()

    class AccountViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.account_row, parent, false))
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.textViewAccountName).text = accountsList[position].name
        val amount = accountsList[position].amount.toString()
        val currency = accountsList[position].currency
        holder.itemView.findViewById<TextView>(R.id.textViewAccountAmount).text = "$amount $currency"
    }

    override fun getItemCount(): Int {
        return accountsList.size
    }

    fun setData(account: List<Account>) {
        accountsList = account
        notifyDataSetChanged()
    }

}