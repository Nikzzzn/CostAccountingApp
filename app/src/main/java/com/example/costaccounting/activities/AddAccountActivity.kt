package com.example.costaccounting.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.costaccounting.helpers.Utils
import com.example.costaccounting.data.Account
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.ActivityAddAccountBinding

import android.content.Intent
import com.example.costaccounting.R

private lateinit var binding: ActivityAddAccountBinding
private lateinit var dataViewModel: DataViewModel
private var firstRun = true

class AddAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        setSupportActionBar(binding.toolbarAddAccount)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.addAccountActivityTitle)

        binding.buttonAccountAdd.setOnClickListener{
            insertDataToDatabase()
        }

        binding.editTextAccountCurrency.setOnClickListener{
            val intentWithResult = Intent(this, ChooseCurrencyActivity::class.java)
            startActivityForResult(intentWithResult, 2)
        }

        firstRun = intent.getBooleanExtra("firstRun", false)

        if(firstRun){
            creatingFirstAccountDialog().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                val result = data?.getStringExtra("currency")
                binding.editTextAccountCurrency.setText(result)
            }
        }
    }

    private fun insertDataToDatabase() {
        val name = binding.editTextAccountName.text.toString()
        val amount = binding.editTextAccountAmount.text.toString()
        val currency = binding.editTextAccountCurrency.text.toString()

        if(inputCheck(name, amount, currency)){
            val account = Account(0, name, amount.toDouble(), currency)
            dataViewModel.addAccount(account)
            if(firstRun){
                baseCurrencyDialog(currency).show()
                val prefs = getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE)
                prefs.edit().putString(Utils.PREF_BASE_CURRENCY_KEY, currency).apply()
                val intent = Intent()
                intent.putExtra("amount", amount)
                intent.putExtra("currency", currency)
                setResult(RESULT_OK, intent)
            } else {
                Toast.makeText(applicationContext, getString(R.string.successfulOperation), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun inputCheck(name: String, amount: String, currency: String): Boolean{
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(amount) && TextUtils.isEmpty(currency))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun baseCurrencyDialog(currency: String): AlertDialog{
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }
        builder.setMessage(String.format(getString(R.string.baseCurrencyMessage), currency))
               .setTitle(getString(R.string.baseCurrencyTitle))
               .setCancelable(false)
        builder.apply {
            setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
                finish()
            }
        }
        return builder.create()
    }

    private fun creatingFirstAccountDialog(): AlertDialog{
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }
        builder.setMessage(String.format(getString(R.string.creatingFirstAccountMessage)))
            .setTitle(getString(R.string.creatingFirstAccountTitle))
            .setCancelable(false)
        builder.apply {
            setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
            }
        }
        return builder.create()
    }

}