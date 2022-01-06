package com.example.costaccounting

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val fragments = arrayOf<Fragment>(ExpensesFragment(), IncomesFragment())
    val fragmentNames = arrayOf("Expenses", "Incomes")

    override fun getItemCount(): Int{ return fragmentNames.size}

    override fun createFragment(position: Int): Fragment {return fragments[position]}
}