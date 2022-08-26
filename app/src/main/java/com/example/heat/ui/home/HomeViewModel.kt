package com.example.heat.ui.home

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.heat.R
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.datamodel.recipeList.RecipeList
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.util.UiUtils.Companion.getURLForResource
import com.example.heat.util.UiUtils.Companion.stringFromResourcesByName
import com.example.heat.util.lazyDeferred
import java.security.AccessController.getContext

class HomeViewModel(
    private val recipesRepository: RecipesRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {


    fun generateDayFakeData(): List<RecipeListItem> {
        val breakfast = RecipeListItem(
            1,
            getURLForResource(R.drawable.breakfast),
            "PNG",
            "Breakfast"
        )
        val lunch = RecipeListItem(
            2,
            getURLForResource(R.drawable.lunch),
            "PNG",
            "Lunch"
        )
        val dinner = RecipeListItem(
            3,
            getURLForResource(R.drawable.dinner),
            "PNG",
            "Dinner"
        )
        val snack = RecipeListItem(
            4,
            getURLForResource(R.drawable.snack),
            "PNG",
            "Snack"
        )

        return listOf(breakfast,lunch,dinner,snack)
    }

    val mealDbSize by lazyDeferred {
        roomRepository.eatMealDBSize()
    }
}