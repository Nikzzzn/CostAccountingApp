package com.example.costaccounting.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.costaccounting.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.costaccounting.BuildConfig
import com.example.costaccounting.R
import com.example.costaccounting.data.Account
import com.example.costaccounting.helpers.Util
import com.example.costaccounting.data.DataViewModel
import com.example.costaccounting.data.USDExchangeRate
import com.example.costaccounting.fragments.TransactionsFragment
import com.example.costaccounting.fragments.AccountsFragment
import com.example.costaccounting.fragments.ThirdFragment
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode


private lateinit var binding: ActivityMainBinding
private lateinit var drawerLayout: DrawerLayout
private lateinit var navView: NavigationView
private lateinit var drawerToggle: ActionBarDrawerToggle
private lateinit var transactionsTextViewName: TextView
private lateinit var transactionsTextViewAmount: TextView
private lateinit var transactionsConstraintLayout: ConstraintLayout
private lateinit var dataViewModel: DataViewModel
private lateinit var accounts: List<Account>
private var selectedId = -1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkFirstRun()
        updateExchangeRates()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.getAllAccounts.observe(this, {
            accounts = it
        })

        val toolbar = binding.toolbarMainInc.toolbarMain
        transactionsTextViewName = toolbar.findViewById(R.id.toolbar_mainTextViewName)
        transactionsTextViewAmount = toolbar.findViewById(R.id.toolbar_mainTextViewAmount)
        transactionsConstraintLayout = toolbar.findViewById(R.id.toolbar_mainConstraintLayout)
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        setSupportActionBar(toolbar)
        setupDrawerContent(navView)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        drawerLayout.addDrawerListener(drawerToggle)

        if (savedInstanceState == null) {
            selectDrawerItem(navView.menu.findItem(R.id.nav_transactions_fragment))
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            selectDrawerItem(it)
            true
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        menuItem.isChecked = true
        var fragment: Fragment? = null

        val fragmentClass = when (menuItem.itemId) {
            R.id.nav_transactions_fragment -> TransactionsFragment::class.java
            R.id.nav_accounts_fragment -> AccountsFragment::class.java
            R.id.nav_third_fragment -> ThirdFragment::class.java
            else -> TransactionsFragment::class.java
        }

        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if(fragment is TransactionsFragment){
            supportActionBar?.setDisplayShowTitleEnabled(false)
            transactionsTextViewName.visibility = VISIBLE
            transactionsTextViewName.text = "All accounts"
            transactionsConstraintLayout.setOnClickListener{textViewAccountClickListener()}

            transactionsTextViewAmount.visibility = VISIBLE
            val prefs = this.getSharedPreferences(Util.PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
            val baseCurrency = prefs!!.getString(Util.PREF_BASE_CURRENCY_KEY, "USD")!!
            dataViewModel.getTotalSumForAllAccounts(baseCurrency).observe(this, Observer {
                val amount = BigDecimal(it).setScale(2, RoundingMode.HALF_EVEN)
                transactionsTextViewAmount.text = "$amount $baseCurrency"
            })

            val arguments = Bundle()
            arguments.putInt("selectedId", selectedId)
            fragment.arguments = arguments
        } else {
            supportActionBar?.setDisplayShowTitleEnabled(true)
            transactionsTextViewName.visibility = GONE
            transactionsTextViewAmount.visibility = GONE
            binding.toolbarMainInc.toolbarMain.title = menuItem.title
        }

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_content, fragment!!).commit()

        drawerLayout.closeDrawers()

    }

    private fun textViewAccountClickListener(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose account")
        var items = arrayOf("All accounts")
        for(acc: String in accounts.map{it.name}){
            items = items.plus(acc)
        }
        builder.setItems(items) { dialog, which ->
            selectedId = if(which == 0){
                -1
            } else{
                accounts[which - 1].id
            }
            transactionsTextViewName.text = items[which]

            val prefs = this.getSharedPreferences(Util.PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
            val baseCurrency = prefs!!.getString(Util.PREF_BASE_CURRENCY_KEY, "USD")!!
            if(selectedId == -1){
                dataViewModel.getTotalSumForAllAccounts(baseCurrency).observe(this, {
                    val amount = BigDecimal(it).setScale(2, RoundingMode.HALF_EVEN)
                    transactionsTextViewAmount.text = "$amount $baseCurrency"
                })
            } else{
                dataViewModel.getTotalSumForAccountById(baseCurrency, selectedId).observe(this, {
                    val amount = BigDecimal(it).setScale(2, RoundingMode.HALF_EVEN)
                    transactionsTextViewAmount.text = "$amount $baseCurrency"
                })
            }

            val arguments = Bundle()
            arguments.putInt("selectedId", selectedId)
            val fragment = TransactionsFragment()
            fragment.arguments = arguments
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout_content, fragment).commit()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }


    private fun checkFirstRun() {
        val currentVersionCode = BuildConfig.VERSION_CODE
        val prefs = getSharedPreferences(Util.PREFS_NAME, MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(Util.PREF_VERSION_CODE_KEY, Util.VERSION_DOESNT_EXIST)

        when {
            currentVersionCode == savedVersionCode -> {
                return
            }
            savedVersionCode == Util.VERSION_DOESNT_EXIST || currentVersionCode > savedVersionCode -> {
                val intent = Intent(applicationContext, AddAccountActivity::class.java)
                startActivity(intent)
            }
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(Util.PREF_VERSION_CODE_KEY, currentVersionCode).apply()
    }

    private fun updateExchangeRates(){
        val API_KEY = Util.API_KEY
        val dataViewModel: DataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        val queue = Volley.newRequestQueue(this)

        val queryPath = "https://freecurrencyapi.net/api/v2/latest?apikey=$API_KEY"
        val stringRequest = StringRequest(
            Request.Method.GET, queryPath,
            { response ->
                val data = JSONObject(JSONObject(response).getString("data"))
                val keys: Iterator<String> = data.keys()

                while (keys.hasNext()) {
                    val currency_name = keys.next()
                    val value = data.get(currency_name).toString().toDouble()
                    val usdExchangeRate = USDExchangeRate(currency_name, value)
                    dataViewModel.addUSDExchangeRate(usdExchangeRate)
                }
                Log.d("asdf", "Internet")
            },
            { })
        queue.add(stringRequest)
    }
}