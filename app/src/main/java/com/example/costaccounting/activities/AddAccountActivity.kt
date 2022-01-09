package com.example.costaccounting.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.costaccounting.Util
import com.example.costaccounting.data.Account
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.ActivityAddAccountBinding
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent




private lateinit var binding: ActivityAddAccountBinding
private lateinit var dataViewModel: DataViewModel
private lateinit var prefs: SharedPreferences

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

        binding.editTextAccountCurrency.setOnClickListener{
            val intentWithResult = Intent(this, ChooseCurrencyActivity::class.java)
            startActivityForResult(intentWithResult, 2)
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
            if(firstRun()){
                baseCurrencyDialog(currency).show()
                prefs.edit().putString(Util.PREF_BASE_CURRENCY_KEY, currency).apply()
            } else {
                Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
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
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    private fun firstRun(): Boolean {
        prefs = getSharedPreferences(Util.PREFS_NAME, MODE_PRIVATE)
        val savedBaseCurrency = prefs.getString(
            Util.PREF_BASE_CURRENCY_KEY,
            Util.CURRENCY_DOESNT_EXIST
        )

        if(savedBaseCurrency == Util.CURRENCY_DOESNT_EXIST){
            return true
        }
        return false
    }

    private fun baseCurrencyDialog(currency: String): AlertDialog{
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }
        builder.setMessage("Selected currency ($currency) will be saved as base currency. You can change base currency in settings")
               .setTitle("Base currency")
               .setCancelable(false)
        builder.apply {
            setPositiveButton("OK") { dialog, id ->
                Log.d("asdf", "countdown")
                dialog.dismiss()
                finish()
            }
        }
        return builder.create()
    }

}