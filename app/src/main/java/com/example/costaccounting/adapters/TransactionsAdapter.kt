package com.example.costaccounting.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.R
import com.example.costaccounting.helpers.Util
import com.example.costaccounting.activities.EditTransactionActivity
import com.example.costaccounting.data.TransactionWithAccount
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class TransactionsAdapter(val context: Context): RecyclerView.Adapter<TransactionsAdapter.DataViewHolder>() {

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

    class TransactionItem(var item: TransactionWithAccount) : RecyclerItem() {
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

                holder.itemView.findViewById<TextView>(R.id.textViewTransactionDate).text = date

            }
            RecyclerItem.typeTransaction -> {
                val transaction = (itemsList[position] as TransactionItem).item
                val amount = BigDecimal(transaction.transaction.amount).setScale(2, RoundingMode.HALF_EVEN)
                val categoryId = context.resources.getIdentifier(transaction.categoryName, "string", context.packageName)
                val categoryName = if (categoryId != 0) context.resources.getString(categoryId) else transaction.categoryName
                holder.itemView.findViewById<TextView>(R.id.textViewTransactionCategory).text = categoryName
                holder.itemView.findViewById<TextView>(R.id.textViewTransactionAmount).text = amount.toString()
                holder.itemView.findViewById<TextView>(R.id.textViewTransactionAccount).text = transaction.accountName
                holder.itemView.findViewById<TextView>(R.id.textViewTransactionCurrency).text = transaction.transaction.currency

                holder.itemView.findViewById<ConstraintLayout>(R.id.transactionRow).setOnClickListener{
                    val intent = Intent(context, EditTransactionActivity::class.java)
                    intent.putExtra("transaction", transaction)
                    context.startActivity(intent)
                }
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
        currentDate = transactionsList[0].item.transaction.date
        itemsList = mutableListOf(DateItem(currentDate))
        for (tr in transactionsList) {
            if (currentDate == tr.item.transaction.date) {
                itemsList.add(tr)
            } else {
                currentDate = tr.item.transaction.date
                itemsList.add(DateItem(tr.item.transaction.date))
                itemsList.add(tr)
            }
        }
    }

    fun setData(transaction: List<TransactionWithAccount>){
        transactionsList = transaction.map {
            TransactionItem(it)
        }
        if(transactionsList.isNotEmpty()){
            updateRecyclerItems()
        } else{
            itemsList = mutableListOf()
        }
        notifyDataSetChanged()
    }

}