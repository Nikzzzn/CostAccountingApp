package com.example.costaccounting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.R
import com.example.costaccounting.adapters.CategoriesAdapter
import com.example.costaccounting.data.DataViewModel

private lateinit var dataViewModel: DataViewModel

class ExpenseCategoryFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_categories, container, false)

        val adapter = CategoriesAdapter(requireContext())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewExpenseCategories)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.getExpenseCategories.observe(viewLifecycleOwner, { categories ->
            var items = mutableListOf<String>()
            for(category in categories.map{ it.name }){
                val id = resources.getIdentifier(category, "string", activity?.packageName)
                if(id != 0){
                    items.add(resources.getString(id))
                } else{
                    items.add(category)
                }
            }
            adapter.setData(categories)
            adapter.setLabels(items)
        })

        return view
    }

}