package com.example.costaccounting.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.costaccounting.R
import com.example.costaccounting.fragments.ExpenseTransactionsFragment
import com.example.costaccounting.fragments.IncomeTransactionsFragment

class ViewPagerTransactionsAdapter(fragment: Fragment, selectedId: Int): FragmentStateAdapter(fragment) {
    private val expensesFragment: ExpenseTransactionsFragment
    private val incomesFragment: IncomeTransactionsFragment
    init{
        val arguments = Bundle()
        arguments.putInt("selectedId", selectedId)
        expensesFragment = ExpenseTransactionsFragment()
        incomesFragment = IncomeTransactionsFragment()
        expensesFragment.arguments = arguments
        incomesFragment.arguments = arguments
    }
    private val fragments = arrayOf(expensesFragment, incomesFragment)
    val fragmentNames = arrayOf(fragment.getString(R.string.expenses), fragment.getString(R.string.incomes))

    override fun getItemCount(): Int{ return fragmentNames.size}

    override fun createFragment(position: Int): Fragment {return fragments[position]}
}