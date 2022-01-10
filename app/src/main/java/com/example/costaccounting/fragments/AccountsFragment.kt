package com.example.costaccounting.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.costaccounting.activities.AddAccountActivity
import com.example.costaccounting.Util
import com.example.costaccounting.adapters.AccountsAdapter
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.FragmentAccountsBinding
import java.math.BigDecimal
import java.math.RoundingMode

class AccountsFragment : Fragment() {

    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: FragmentAccountsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)

        val adapter = AccountsAdapter(requireContext())
        val recyclerView = binding.recyclerViewAccounts
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.getAllAccounts.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        val prefs = activity?.getSharedPreferences(Util.PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val baseCurrency = prefs!!.getString(Util.PREF_BASE_CURRENCY_KEY, "USD")!!
        dataViewModel.getTotalSumForAllAccounts(baseCurrency).observe(viewLifecycleOwner, Observer {
            val amount = BigDecimal(it).setScale(2, RoundingMode.HALF_EVEN)
            binding.textViewAccountsTotal.text = "$amount $baseCurrency"
        })

        val fab: View = binding.fabAccounts
        fab.setOnClickListener {
            val intent = Intent(context, AddAccountActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }


}