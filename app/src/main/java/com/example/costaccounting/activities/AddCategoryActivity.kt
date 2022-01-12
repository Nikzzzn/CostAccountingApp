package com.example.costaccounting.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.costaccounting.R
import com.example.costaccounting.data.Category
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.ActivityAddCategoryBinding

private lateinit var binding: ActivityAddCategoryBinding
private lateinit var dataViewModel: DataViewModel

class AddCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        setSupportActionBar(binding.toolbarAddCategory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        title = getString(R.string.addCategoryActivityTitle)

        binding.buttonCategoryAdd.setOnClickListener{
            val isAnExpenseCategory = intent.getBooleanExtra("isAnExpenseCategory", true)
            insertDataToDatabase(isAnExpenseCategory)
        }

    }

    private fun insertDataToDatabase(isAnExpenseCategory: Boolean) {
        val name = binding.editTextCategoryName.text.toString()

        if(!TextUtils.isEmpty(name)){
            val category = Category(0, name, isAnExpenseCategory)
            dataViewModel.addCategory(category)
            Toast.makeText(applicationContext, getString(R.string.successfulOperation), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

}