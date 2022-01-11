package com.example.costaccounting.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.costaccounting.fragments.ExpensesFragment
import com.example.costaccounting.fragments.IncomesFragment

class ViewPagerAdapter(fragment: Fragment, selectedId: Int): FragmentStateAdapter(fragment) {
    private val expensesFragment: ExpensesFragment
    private val incomesFragment: IncomesFragment
    init{
        val arguments = Bundle()
        arguments.putInt("selectedId", selectedId)
        expensesFragment = ExpensesFragment()
        incomesFragment = IncomesFragment()
        expensesFragment.arguments = arguments
        incomesFragment.arguments = arguments
    }
    private val fragments = arrayOf<Fragment>(expensesFragment, incomesFragment)
    val fragmentNames = arrayOf("Expenses", "Incomes")

    override fun getItemCount(): Int{ return fragmentNames.size}

    override fun createFragment(position: Int): Fragment {return fragments[position]}
}