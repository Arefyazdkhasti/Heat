package com.example.heat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.heat.R
import com.example.heat.databinding.ActivityMainBinding
import com.example.heat.databinding.ActivityStarterBinding

class StarterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityStarterBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarterBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_staret) as NavHostFragment).navController

    }
}