package com.example.heat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.heat.R
import com.example.heat.databinding.ActivityMainBinding
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum
import android.content.DialogInterface
import android.util.Log
import com.example.heat.util.UiUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)


        //first method for finding navController throw error in navigation-fragment-ktx:2.5.1 or higher
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController


        binding.bottomNav.setupWithNavController(navController)


        //NavigationUI.setupActionBarWithNavController(this, navController)

        //setupActionBarWithNavController(navController)


        initDestinationListener()
    }


    /*override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp()*/

    private fun initDestinationListener() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.e("DESTINATION", " -> $destination")
            // Showing or Hiding Bottom Navigation on Specific Screen
            if (destination.id == R.id.splashScreenFragment ||
                destination.id == R.id.loginFragment ||
                destination.id == R.id.registerFragment ||
                destination.id == R.id.personalDataFragment||
                destination.id == R.id.dietTypeFragment||
                destination.id == R.id.abstractGoalFragment||
                destination.id == R.id.ingredientAllergyFragment||
                destination.id == R.id.diseaseFragment||
                destination.id == R.id.activeLevelFragment ||
                destination.id == R.id.settingsFragment ||
                destination.id == R.id.recipeDetailFragment ||
                destination.id == R.id.surveyFragment ||
                destination.id == R.id.trackFoodsFragment
            ) {
                hideBottomNavigation()
            } else {
                showBottomNavigation()
            }
        }
    }

    private fun showBottomNavigation() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        binding.bottomNav.visibility = View.GONE
    }

}
