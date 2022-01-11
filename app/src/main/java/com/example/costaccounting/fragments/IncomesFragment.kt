package com.example.costaccounting.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.R
import com.example.costaccounting.adapters.TransactionsAdapter
import com.example.costaccounting.data.DataViewModel

class IncomesFragment : Fragment() {

    private lateinit var dataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_incomes, container, false)

        val adapter = TransactionsAdapter(requireContext())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewIncomes)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val bundle = this.arguments
        val selectedId = bundle?.getInt("selectedId", -1)

        if(selectedId == -1){
            dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
            dataViewModel.getAllIncomes.observe(viewLifecycleOwner, {
                adapter.setData(it)
            })
        } else{
            dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
            dataViewModel.getAllIncomesByAccountId(selectedId!!).observe(viewLifecycleOwner, {
                adapter.setData(it)
            })
        }

        return view
    }


}