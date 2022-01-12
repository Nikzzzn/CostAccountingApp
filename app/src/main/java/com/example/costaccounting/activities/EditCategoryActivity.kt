package com.example.costaccounting.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.costaccounting.R
import com.example.costaccounting.data.Account
import com.example.costaccounting.data.Category
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.ActivityEditAccountBinding
import com.example.costaccounting.databinding.ActivityEditCategoryBinding

private lateinit var binding: ActivityEditCategoryBinding
private lateinit var dataViewModel: DataViewModel
private lateinit var category: Category

class EditCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        setSupportActionBar(binding.toolbarEditCategory.toolbarEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        title = getString(R.string.editCategoryActivityTitle)

        category = intent.getParcelableExtra("category")!!
        binding.editTextEditCategoryName.setText(category.name)

        binding.buttonCategorySave.setOnClickListener {
            insertDataToDatabase()
        }
    }

    private fun insertDataToDatabase() {
        val id = category.id
        val isAnExpenseCategory = category.isAnExpenseCategory
        val name = binding.editTextEditCategoryName.text.toString()

        if(!TextUtils.isEmpty(name)){
            val newCategory = Category(id, name, isAnExpenseCategory)
            dataViewModel.updateCategory(newCategory)
        }

        Toast.makeText(applicationContext, getString(R.string.successfulOperation), Toast.LENGTH_SHORT).show()
        this.finish()
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
                deleteAccount()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAccount() {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton(getString(R.string.positiveButton)){ _, _ ->
            dataViewModel.deleteCategory(category)
            Toast.makeText(this,getString(R.string.successfulOperation), Toast.LENGTH_SHORT).show()
            finish()
        }
        builder.setNegativeButton(getString(R.string.negativeButton)){ _, _ -> }
        builder.setTitle(getString(R.string.deleteCategoryTitle))
        builder.setMessage(getString(R.string.deleteCategoryMessage))
        builder.create().show()
    }

}