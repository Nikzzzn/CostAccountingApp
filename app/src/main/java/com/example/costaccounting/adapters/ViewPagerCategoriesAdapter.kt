package com.example.costaccounting.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.costaccounting.R
import com.example.costaccounting.fragments.ExpenseCategoryFragment
import com.example.costaccounting.fragments.IncomeCategoryFragment

class ViewPagerCategoriesAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val fragments = arrayOf(ExpenseCategoryFragment(), IncomeCategoryFragment())
    val fragmentNames = arrayOf(fragment.getString(R.string.expenses), fragment.getString(R.string.incomes))

    override fun getItemCount(): Int{ return fragmentNames.size}

    override fun createFragment(position: Int): Fragment {return fragments[position]}
}