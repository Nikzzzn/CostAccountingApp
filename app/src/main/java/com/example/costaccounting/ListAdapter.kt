package com.example.costaccounting

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.data.Transaction
import java.util.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.DataViewHolder>() {

    private var transactionsList = emptyList<TransactionItem>()
    private var itemsList = mutableListOf<RecyclerItem>()
    private var currentDate: Date = Date()

    open class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    class TransactionViewHolder(itemView: View): DataViewHolder(itemView)
    class DateViewHolder(itemView: View): DataViewHolder(itemView)

    abstract class RecyclerItem{
        companion object {
            const val typeDate = 0
            const val typeTransaction = 1
        }
        abstract fun getType(): Int
    }

    class DateItem(var displayDate: Date) : RecyclerItem() {
        override fun getType(): Int {
            return typeDate
        }
    }

    class TransactionItem(var transaction: Transaction) : RecyclerItem() {
        override fun getType(): Int {
            return typeTransaction
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            RecyclerItem.typeDate -> DateViewHolder(inflater.inflate(R.layout.date_row, parent, false))
            RecyclerItem.typeTransaction -> TransactionViewHolder(inflater.inflate(R.layout.transaction_row, parent, false))
            else -> throw RuntimeException("No such viewType")
        }
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        when(holder.itemViewType){
            RecyclerItem.typeDate -> {
                val dateItem : DateItem = itemsList[position] as DateItem
                val dayString = Util.getDayDateFormat().format(dateItem.displayDate)
                val monthString = Util.getMonthDateFormat().format(dateItem.displayDate)
                val yearString = Util.getYearDateFormat().format(dateItem.displayDate)
                val date = "$dayString $monthString $yearString"

                holder.itemView.findViewById<TextView>(R.id.textViewDate).text = date

            }
            RecyclerItem.typeTransaction -> {
                val transactionItem = itemsList[position] as TransactionItem
                holder.itemView.findViewById<TextView>(R.id.textViewCategory).text = transactionItem.transaction.category
                holder.itemView.findViewById<TextView>(R.id.textViewAmount).text = transactionItem.transaction.amount.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return itemsList[position].getType()
    }

    private fun updateRecyclerItems(){
        currentDate = transactionsList[0].transaction.date
        itemsList = mutableListOf(DateItem(currentDate))
        for (tr in transactionsList) {
            if (currentDate == tr.transaction.date) {
                itemsList.add(tr)
            } else {
                currentDate = tr.transaction.date
                itemsList.add(DateItem(tr.transaction.date))
                itemsList.add(tr)
            }
        }
        Log.d("asdf", itemsList.toString())
    }

    fun setData(transaction: List<Transaction>){
        transactionsList = transaction.map {
            TransactionItem(it)
        }
        if(transactionsList.isNotEmpty()){
            updateRecyclerItems()
        }
        notifyDataSetChanged()
    }

}