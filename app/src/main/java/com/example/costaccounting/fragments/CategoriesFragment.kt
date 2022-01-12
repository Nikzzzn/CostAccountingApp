package com.example.costaccounting.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.costaccounting.activities.AddCategoryActivity
import com.example.costaccounting.adapters.ViewPagerCategoriesAdapter
import com.example.costaccounting.databinding.FragmentCategoriesBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        val viewPager: ViewPager2 = binding.viewPagerCategories
        val tabLayout: TabLayout = binding.tabLayoutCategories

        val adapter = ViewPagerCategoriesAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) {
                tab, position -> tab.text = adapter.fragmentNames[position]
        }.attach()

        val fab: View = binding.fabCategories
        fab.setOnClickListener {
            val isAnExpenseCategory: Boolean = tabLayout.selectedTabPosition == 0
            val intent = Intent(context, AddCategoryActivity::class.java)
            intent.putExtra("isAnExpenseCategory", isAnExpenseCategory)
            startActivity(intent)
        }

        return binding.root
    }

}
