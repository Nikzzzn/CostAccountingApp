package com.example.costaccounting.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.costaccounting.R
import com.example.costaccounting.data.Account
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.ActivityEditAccountBinding
import java.math.BigDecimal
import java.math.RoundingMode

private lateinit var binding: ActivityEditAccountBinding
private lateinit var dataViewModel: DataViewModel
private lateinit var account: Account


class EditAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        setSupportActionBar(binding.toolbarEditAccount.toolbarEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.editAccountActivityTitle)

        account = intent.getParcelableExtra("account")!!
        val amount = BigDecimal(account.amount).setScale(2, RoundingMode.HALF_EVEN)
        binding.editTextEditAccountAmount.setText(amount.toString())
        binding.editTextEditAccountName.setText(account.name)
        binding.editTextEditAccountCurrency.setText(account.currency)

        binding.buttonAccountSave.setOnClickListener {
            insertDataToDatabase()
        }

        binding.editTextEditAccountCurrency.setOnClickListener{
            val intentWithResult = Intent(this, ChooseCurrencyActivity::class.java)
            startActivityForResult(intentWithResult, 5)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                val result = data?.getStringExtra("currency")
                binding.editTextEditAccountCurrency.setText(result)
            }
        }
    }

    private fun insertDataToDatabase() {
        val id = account.id
        val name = binding.editTextEditAccountName.text.toString()
        val amount = binding.editTextEditAccountAmount.text.toString()
        val currency = binding.editTextEditAccountCurrency.text.toString()

        if(inputCheck(name, amount, currency)){
            val newAccount = Account(id, name, amount.toDouble(), currency)
            dataViewModel.updateAccount(newAccount)
        }

        Toast.makeText(applicationContext, getString(R.string.successfulOperation), Toast.LENGTH_SHORT).show()
        this.finish()
    }

    private fun inputCheck(name: String, amount: String, currency: String): Boolean{
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(amount) && TextUtils.isEmpty(currency))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.delete_item -> {
                deleteAccount()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAccount() {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton(getString(R.string.positiveButton)){ _, _ ->
            dataViewModel.deleteAccount(account)
            Toast.makeText(this,getString(R.string.successfulOperation), Toast.LENGTH_SHORT).show()
            finish()
        }
        builder.setNegativeButton(getString(R.string.negativeButton)){ _, _ -> }
        builder.setTitle(getString(R.string.deleteAccountTitle))
        builder.setMessage(getString(R.string.deleteAccountMessage))
        builder.create().show()
    }

}