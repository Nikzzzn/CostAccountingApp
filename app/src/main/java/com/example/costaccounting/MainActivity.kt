package com.example.costaccounting

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.costaccounting.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import android.content.SharedPreferences




private lateinit var binding: ActivityMainBinding
private lateinit var drawerLayout: DrawerLayout
private lateinit var navView: NavigationView
private lateinit var drawerToggle: ActionBarDrawerToggle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkFirstRun();

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
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
            selectDrawerItem(navView.menu.findItem(R.id.nav_first_fragment))
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
        var fragment: Fragment? = null

        val fragmentClass = when (menuItem.itemId) {
            R.id.nav_first_fragment -> FirstFragment::class.java
            R.id.nav_second_fragment -> SecondFragment::class.java
            R.id.nav_third_fragment -> ThirdFragment::class.java
            else -> FirstFragment::class.java
        }

        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_content, fragment!!).commit()

        menuItem.isChecked = true
        title = menuItem.title
        drawerLayout.closeDrawers()

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
        val PREFS_NAME = "MyPrefsFile"
        val PREF_VERSION_CODE_KEY = "version_code"
        val DOESNT_EXIST = -1

        val currentVersionCode = BuildConfig.VERSION_CODE

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST)

        when {
            currentVersionCode == savedVersionCode -> {
                return
            }
            savedVersionCode == DOESNT_EXIST || currentVersionCode > savedVersionCode -> {
                val intent = Intent(applicationContext, AddAccountActivity::class.java)
                startActivity(intent)
            }
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()
    }
}