package com.example.costaccounting.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.costaccounting.databinding.ActivityEditTransactionBinding
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.costaccounting.R
import com.example.costaccounting.helpers.Util
import com.example.costaccounting.activities.*
import com.example.costaccounting.data.Account
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.data.Transaction
import com.example.costaccounting.data.TransactionWithAccount
import java.util.*

private lateinit var binding: ActivityEditTransactionBinding
private lateinit var dataViewModel: DataViewModel
private val myCalendar: Calendar = Calendar.getInstance()
private var selectedAccount: Account? = null
private lateinit var accounts: List<Account>
private lateinit var oldAccount: Account
private lateinit var transaction: Transaction

class EditTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        setSupportActionBar(binding.toolbarEditTransaction.toolbarEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        title = "Edit transaction"

        val transactionWithAccount = intent.getParcelableExtra<TransactionWithAccount>("transaction")
        transaction = transactionWithAccount!!.transaction
        binding.editTextEditTransactionAmount.setText(transaction.amount.toString())
        binding.editTextEditTransactionAccount.setText(transactionWithAccount.accountName.toString())
        binding.editTextEditTransactionCurrency.setText(transaction.currency)
        binding.editTextEditTransactionCategory.setText(transaction.category)
        binding.editTextEditTransactionDate.setText(
            Util.getFullDateFormat().format(transaction.date))
        dataViewModel.getAccountById(transaction.account_id).observe(this, {
            oldAccount = it
        })

        binding.buttonTransactionSave.setOnClickListener {
            insertDataToDatabase()
        }

        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = month
                myCalendar[Calendar.DAY_OF_MONTH] = day
                updateLabel()
            }
        binding.editTextEditTransactionDate.setOnClickListener {
            DatePickerDialog(
                this@EditTransactionActivity,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        dataViewModel.getAllAccounts.observe(this, {
            accounts = it
        })

        binding.editTextEditTransactionAccount.setOnClickListener{
            editTextAccountClickListener()
        }

        binding.editTextEditTransactionCurrency.setOnClickListener{
            val intentWithResult = Intent(this, ChooseCurrencyActivity::class.java)
            startActivityForResult(intentWithResult, 4)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 4) {
            if (resultCode == RESULT_OK) {
                val result = data?.getStringExtra("currency")
                binding.editTextEditTransactionCurrency.setText(result)
            }
        }
    }

    private fun editTextAccountClickListener(){
        val builder = AlertDialog.Builder(this@EditTransactionActivity)
        builder.setTitle("Choose account")
        builder.setItems(accounts.map{it.name}.toTypedArray()) { dialog, which ->
            selectedAccount = accounts[which]
            binding.editTextEditTransactionAccount.setText(
                selectedAccount!!.name)
            binding.editTextEditTransactionCurrency.setText(
                selectedAccount!!.currency)
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateLabel() {
        binding.editTextEditTransactionDate.setText(Util.getFullDateFormat().format(myCalendar.time))
    }

    private fun insertDataToDatabase() {
        val id = transaction.id
        val isAnExpense = transaction.isAnExpense
        val amount = binding.editTextEditTransactionAmount.text.toString()
        val account = binding.editTextEditTransactionAccount.text.toString()
        val currency = binding.editTextEditTransactionCurrency.text.toString()
        val category = binding.editTextEditTransactionCategory.text.toString()
        val date = binding.editTextEditTransactionDate.text.toString()

        if(inputCheck(amount, account, currency, category, date)){
            val oldConvertedAmount = Util.convertCurrency(this, transaction.currency, oldAccount.currency, transaction.amount)
            if(selectedAccount != null && selectedAccount!!.id != oldAccount.id){
                val transaction = Transaction(id, isAnExpense, amount.toDouble(), selectedAccount!!.id,
                    currency, category, Util.getFullDateFormat().parse(date)!!)
                dataViewModel.updateTransaction(transaction)

                val convertedAmount = Util.convertCurrency(this, currency, selectedAccount!!.currency, amount.toDouble())
                var newAmount = 0.0
                var oldAccountNewAmount = 0.0
                if(isAnExpense){
                    newAmount = selectedAccount!!.amount - convertedAmount
                    oldAccountNewAmount = oldAccount.amount + oldConvertedAmount
                } else{
                    newAmount = selectedAccount!!.amount + convertedAmount
                    oldAccountNewAmount = oldAccount.amount - oldConvertedAmount
                }
                val newAccount = Account(selectedAccount!!.id, selectedAccount!!.name, newAmount, selectedAccount!!.currency)
                dataViewModel.updateAccount(newAccount)
                val newOldAccount = Account(oldAccount.id, oldAccount.name, oldAccountNewAmount, oldAccount.currency)
                dataViewModel.updateAccount(newOldAccount)
            } else {
                val transaction = Transaction(id, isAnExpense, amount.toDouble(), oldAccount.id,
                    currency, category, Util.getFullDateFormat().parse(date)!!)
                dataViewModel.updateTransaction(transaction)

                val convertedAmount = Util.convertCurrency(this, currency, oldAccount.currency, amount.toDouble())
                val difference = oldConvertedAmount - convertedAmount
                val oldAccountNewAmount = if(isAnExpense){
                    oldAccount.amount + difference
                } else{
                    oldAccount.amount - difference
                }
                val newOldAccount = Account(oldAccount.id, oldAccount.name, oldAccountNewAmount, oldAccount.currency)
                dataViewModel.updateAccount(newOldAccount)
            }

            Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }

    private fun inputCheck(amount: String, account: String, currency: String, category: String, date: String): Boolean{
        return !(TextUtils.isEmpty(amount) &&
                TextUtils.isEmpty(account) &&
                TextUtils.isEmpty(currency) &&
                TextUtils.isEmpty(category) &&
                TextUtils.isEmpty(date))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true;
            }
            R.id.delete_item -> {
                deleteTransaction()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTransaction() {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Yes"){ _, _ ->
            val amount = Util.convertCurrency(this, transaction.currency, oldAccount.currency, transaction.amount)
            val oldAccountNewAmount = if(transaction.isAnExpense){
                oldAccount.amount + amount
            } else{
                oldAccount.amount - amount
            }
            dataViewModel.deleteTransaction(transaction)
            val newOldAccount = Account(oldAccount.id, oldAccount.name, oldAccountNewAmount, oldAccount.currency)
            dataViewModel.updateAccount(newOldAccount)
            Toast.makeText(this,"Success!", Toast.LENGTH_SHORT).show()
            finish()
        }
        builder.setNegativeButton("No"){ _, _ -> }
        builder.setTitle("Delete transaction?")
        builder.setMessage("Are you sure you want to delete this transaction?")
        builder.create().show()
    }

}