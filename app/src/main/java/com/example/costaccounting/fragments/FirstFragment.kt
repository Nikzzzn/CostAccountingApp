package com.example.costaccounting.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.costaccounting.databinding.FragmentFirstBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.example.costaccounting.activities.AddTransactionActivity
import com.example.costaccounting.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)

        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) {
                tab, position -> tab.text = adapter.fragmentNames[position]

        }.attach()

        val fab: View = binding.fabTransactions
        fab.setOnClickListener {
            val isAnExpense: Boolean = tabLayout.selectedTabPosition == 0
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra("isAnExpense", isAnExpense)
            startActivity(intent)
        }

        return binding.root
    }

}