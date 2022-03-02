package com.klasha.klashaapp2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.klasha.android.model.Country
import com.klasha.android.model.Currency
import com.klasha.klashaapp2.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_card,
                R.id.navigation_bank,
                R.id.navigation_klasha_wallet,
                R.id.navigation_mobile_money,
                R.id.navigation_mpesa
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.countries.value = ArrayList<String>().apply {
            Country.values().forEach {
                val ctr = it.name.replace("_"," ").lowercase()
                    .replaceFirstChar { char->if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
                this.add(ctr)
            }
            this.sort()
        }
        mainActivityViewModel.currencies.value = ArrayList<String>().apply {
            Currency.values().forEach {
                this.add(it.name)
            }
            this.sort()
        }

    }
}