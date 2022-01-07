package com.example.costaccounting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.data.Transaction

class ListAdapter: RecyclerView.Adapter<ListAdapter.TransactionViewHolder>() {

    private var transactionsList = emptyList<Transaction>()

    class TransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_row, parent, false))
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentItem = transactionsList[position]
        holder.itemView.findViewById<TextView>(R.id.textViewCategory).text = currentItem.category
        holder.itemView.findViewById<TextView>(R.id.textViewDate).text = currentItem.date.toString()
        holder.itemView.findViewById<TextView>(R.id.textViewAmount).text = currentItem.amount.toString()
    }

    override fun getItemCount(): Int {
        return transactionsList.size
    }

    fun setData(transaction: List<Transaction>){
        this.transactionsList = transaction
        notifyDataSetChanged()
    }

}