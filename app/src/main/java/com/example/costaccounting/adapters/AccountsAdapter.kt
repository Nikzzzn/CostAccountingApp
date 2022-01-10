package com.example.costaccounting.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.R
import com.example.costaccounting.activities.EditAccountActivity
import com.example.costaccounting.activities.EditTransactionActivity
import com.example.costaccounting.data.Account
import java.math.BigDecimal
import java.math.RoundingMode

class AccountsAdapter(val context: Context): RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    private var accountsList = emptyList<Account>()

    class AccountViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.account_row, parent, false))
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accountsList[position]
        holder.itemView.findViewById<TextView>(R.id.textViewAccountName).text = account.name
        val amount = BigDecimal(account.amount).setScale(2, RoundingMode.HALF_EVEN)
        val currency = account.currency
        holder.itemView.findViewById<TextView>(R.id.textViewAccountAmount).text = "$amount $currency"

        holder.itemView.findViewById<CardView>(R.id.accountRow).setOnClickListener{
            val intent = Intent(context, EditAccountActivity::class.java)
            intent.putExtra("account", account)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return accountsList.size
    }

    fun setData(account: List<Account>) {
        accountsList = account
        notifyDataSetChanged()
    }

}