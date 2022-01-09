package com.example.costaccounting.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.costaccounting.R
import com.example.costaccounting.adapters.CurrencyAdapter
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.ActivityChooseCurrencyBinding
import com.example.costaccounting.databinding.ActivityMainBinding

private lateinit var binding: ActivityChooseCurrencyBinding
private lateinit var dataViewModel: DataViewModel

class ChooseCurrencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseCurrencyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val adapter = CurrencyAdapter(this)
        val recyclerView = binding.recyclerViewChooseCurrency
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.getAllCurrencies.observe(this, {
            adapter.setData(it)
        })
    }
}