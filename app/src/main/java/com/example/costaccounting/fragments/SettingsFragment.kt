package com.example.costaccounting.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.costaccounting.activities.ChooseCurrencyActivity
import com.example.costaccounting.databinding.FragmentSettingsBinding
import com.example.costaccounting.helpers.Utils

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        prefs = activity?.getSharedPreferences(Utils.PREFS_NAME, AppCompatActivity.MODE_PRIVATE)!!
        val baseCurrency = prefs.getString(Utils.PREF_BASE_CURRENCY_KEY, "USD")!!

        binding.textViewBaseCurrency.text = baseCurrency
        binding.cardViewSettingsBaseCurrency.setOnClickListener{
            val intentWithResult = Intent(requireContext(), ChooseCurrencyActivity::class.java)
            startActivityForResult(intentWithResult, 6)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 6) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val result = data?.getStringExtra("currency")
                prefs.edit().putString(Utils.PREF_BASE_CURRENCY_KEY, result).apply()
                binding.textViewBaseCurrency.text = result
            }
        }
    }

}
