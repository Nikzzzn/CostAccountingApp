package com.example.costaccounting.activities

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
import android.content.Intent
import com.example.costaccounting.R
import com.example.costaccounting.helpers.Utils
import com.example.costaccounting.data.Account
import com.example.costaccounting.data.Category

private lateinit var binding: ActivityAddTransactionBinding
private lateinit var dataViewModel: DataViewModel
private val myCalendar: Calendar = Calendar.getInstance()
private var selectedAccount: Account? = null
private var selectedCategory: Category? = null

class AddTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        setSupportActionBar(binding.toolbarAddTransaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.addTransactionActivityTitle)

        val isAnExpense = intent.getBooleanExtra("isAnExpense", true)
        binding.buttonTransactionAdd.setOnClickListener{
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

        var accounts: List<Account>? = null
        dataViewModel.getAllAccounts.observe(this, {
            accounts = it
        })
        var categories: List<Category>? = null
        if(isAnExpense) {
            dataViewModel.getExpenseCategories.observe(this, {
                categories = it
            })
        } else{
            dataViewModel.getIncomeCategories.observe(this, {
                categories = it
            })
        }

        binding.editTextTransactionAccount.setOnClickListener{
            val builder = AlertDialog.Builder(this@AddTransactionActivity)
            builder.setTitle(getString(R.string.accountSelectorTitle))
            builder.setItems(accounts?.map{it.name}?.toTypedArray()) { dialog, which ->
                selectedAccount = accounts?.get(which)
                binding.editTextTransactionAccount.setText(selectedAccount?.name)
                binding.editTextTransactionCurrency.setText(selectedAccount?.currency)
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.editTextTransactionCategory.setOnClickListener{
            val builder = AlertDialog.Builder(this@AddTransactionActivity)
            builder.setTitle(R.string.categorySelectorTitle)
            var items = arrayOf<String>()
            for(category in categories?.map{ it.name }!!){
                val id = resources.getIdentifier(category, "string", packageName)
                if(id != 0){
                    items = items.plus(resources.getString(id))
                } else{
                    items = items.plus(category)
                }
            }
            builder.setItems(items) { dialog, which -> //categories!!.map{it.name}.toTypedArray()
                selectedCategory = categories?.get(which)
                binding.editTextTransactionCategory.setText(items[which])
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.editTextTransactionCurrency.setOnClickListener{
            val intentWithResult = Intent(this, ChooseCurrencyActivity::class.java)
            startActivityForResult(intentWithResult, 3)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                val result = data?.getStringExtra("currency")
                binding.editTextTransactionCurrency.setText(result)
            }
        }
    }

    private fun updateLabel() {
        binding.editTextTransactionDate.setText(Utils.getFullDateFormat().format(myCalendar.time))
    }

    private fun insertDataToDatabase(isAnExpense: Boolean) {
        val amount = binding.editTextTransactionAmount.text.toString()
        val account = binding.editTextTransactionAccount.text.toString()
        val currency = binding.editTextTransactionCurrency.text.toString()
        val category = binding.editTextTransactionCategory.text.toString()
        val date = binding.editTextTransactionDate.text.toString()

        if(inputCheck(amount, account, currency, category, date)){
            val transaction = Transaction(0, isAnExpense, amount.toDouble(), selectedAccount!!.id,
                currency, selectedCategory!!.id, Utils.getFullDateFormat().parse(date)!!)
            dataViewModel.addTransaction(transaction)

            val convertedAmount = Utils.convertCurrency(this, currency, selectedAccount!!.currency, amount.toDouble())
            val newAmount = if(isAnExpense){
                selectedAccount!!.amount - convertedAmount
            } else{
                selectedAccount!!.amount + convertedAmount
            }
            val newAccount = Account(selectedAccount!!.id, selectedAccount!!.name, newAmount, selectedAccount!!.currency)
            dataViewModel.updateAccount(newAccount)
            Toast.makeText(applicationContext, getString(R.string.successfulOperation), Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }

    private fun inputCheck(amount: String, account: String, currency: String, category: String, date: String): Boolean{
        return !(TextUtils.isEmpty(amount) ||
                 TextUtils.isEmpty(account) ||
                 TextUtils.isEmpty(currency) ||
                 TextUtils.isEmpty(category) ||
                 TextUtils.isEmpty(date))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}