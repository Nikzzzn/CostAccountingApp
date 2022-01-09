package com.example.costaccounting.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.costaccounting.fragments.ExpensesFragment
import com.example.costaccounting.fragments.IncomesFragment

class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val fragments = arrayOf<Fragment>(ExpensesFragment(), IncomesFragment())
    val fragmentNames = arrayOf("Expenses", "Incomes")

    override fun getItemCount(): Int{ return fragmentNames.size}

    override fun createFragment(position: Int): Fragment {return fragments[position]}
}