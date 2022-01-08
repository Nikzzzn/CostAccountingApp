package com.example.costaccounting

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.data.Transaction
import com.example.costaccounting.databinding.ActivityAddTransactionBinding
import java.util.*
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.util.Log
import com.example.costaccounting.data.Account

private lateinit var binding: ActivityAddTransactionBinding
private lateinit var dataViewModel: DataViewModel
private val myCalendar: Calendar = Calendar.getInstance()
private var selectedAccount: Int? = null
private lateinit var accounts: List<Account>

class AddTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        setSupportActionBar(binding.toolbarAddTransaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        title = "New transaction"

        binding.buttonTransactionAdd.setOnClickListener{
            val isAnExpense = intent.getBooleanExtra("isAnExpense", true)
            insertDataToDatabase(isAnExpense)
        }

        val date =
            OnDateSetListener { _, year, month, day ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = month
                myCalendar[Calendar.DAY_OF_MONTH] = day
                updateLabel()
            }
        binding.editTextTransactionDate.setOnClickListener {
            DatePickerDialog(
                this@AddTransactionActivity,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        dataViewModel.readAllAccounts.observe(this, {
            accounts = it
        })

        binding.editTextTransactionAccount.setOnClickListener{
            val builder = AlertDialog.Builder(this@AddTransactionActivity)
            builder.setTitle("Choose account")
            builder.setItems(accounts.map{it.name}.toTypedArray()) { dialog, which ->
                binding.editTextTransactionAccount.setText(accounts[which].name)
                selectedAccount = accounts[which].id
                Log.d("asdf", selectedAccount.toString())
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun updateLabel() {
        binding.editTextTransactionDate.setText(Util.getFullDateFormat().format(myCalendar.time))
    }

    private fun insertDataToDatabase(isAnExpense: Boolean) {
        val amount = binding.editTextTransactionAmount.text.toString()
        val category = binding.editTextTransactionCategory.text.toString()
        val date = binding.editTextTransactionDate.text.toString()

        if(inputCheck(amount, category, date)){
            val transaction = Transaction(0, isAnExpense, amount.toDouble(), selectedAccount!!, category, Util.getFullDateFormat().parse(date)!!)
            dataViewModel.addTransaction(transaction)
            Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }

    private fun inputCheck(amount: String, category: String, date: String): Boolean{
        return !(TextUtils.isEmpty(amount) && TextUtils.isEmpty(category) && TextUtils.isEmpty(date))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}