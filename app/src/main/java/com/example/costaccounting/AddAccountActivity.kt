package com.example.costaccounting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.costaccounting.data.Account
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.data.Transaction
import com.example.costaccounting.databinding.ActivityAddAccountBinding
import com.example.costaccounting.databinding.ActivityAddTransactionBinding

private lateinit var binding: ActivityAddAccountBinding
private lateinit var dataViewModel: DataViewModel

class AddAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        setSupportActionBar(binding.toolbarAddAccount)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        title = "New account"

        binding.buttonAccountAdd.setOnClickListener{
            insertDataToDatabase()
        }
    }

    private fun insertDataToDatabase() {
        val name = binding.editTextAccountName.text.toString()
        val amount = binding.editTextAccountAmount.text.toString()
        val currency = binding.editTextAccountCurrency.text.toString()

        if(inputCheck(name, amount, currency)){
            val account = Account(0, name, amount.toDouble(), currency)
            dataViewModel.addAccount(account)
            Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }

    private fun inputCheck(name: String, amount: String, currency: String): Boolean{
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(amount) && TextUtils.isEmpty(currency))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

}