package com.example.costaccounting.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.costaccounting.R
import com.example.costaccounting.adapters.CurrencyAdapter
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.ActivityChooseCurrencyBinding
import com.example.costaccounting.databinding.ActivityMainBinding

private lateinit var binding: ActivityChooseCurrencyBinding
private lateinit var dataViewModel: DataViewModel
private lateinit var adapter: CurrencyAdapter

class ChooseCurrencyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseCurrencyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarChooseCurrency)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        title = getString(R.string.chooseCurrencyActivityTitle)

        adapter = CurrencyAdapter(this)
        val recyclerView = binding.recyclerViewChooseCurrency
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.getAllCurrencies.observe(this, {
            adapter.setData(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choose_currency_menu, menu)
        val search = menu?.findItem(R.id.currency_search)
        val searchView = search?.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

}