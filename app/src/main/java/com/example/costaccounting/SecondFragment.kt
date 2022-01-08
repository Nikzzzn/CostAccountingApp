package com.example.costaccounting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.databinding.FragmentFirstBinding
import com.example.costaccounting.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(inflater, container, false)

        val adapter = AccountsAdapter()
        val recyclerView = binding.recyclerViewAccounts
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.readAllAccounts.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        val fab: View = binding.fabAccounts
        fab.setOnClickListener {
            val intent = Intent(context, AddAccountActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }


}