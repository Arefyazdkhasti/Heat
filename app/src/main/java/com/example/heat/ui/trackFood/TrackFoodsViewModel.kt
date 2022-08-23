package com.example.heat.ui.trackFood

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.R
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.util.UiUtils.Companion.getStringFromResource
import com.example.heat.util.lazyDeferred
import kotlinx.coroutines.launch
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*

class TrackFoodsViewModel(
    private val recipesRepository: RecipesRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    fun loadFakeDateToRoom() = viewModelScope.launch{
        val breakfast = MealListItem(
            1,
            "https://simply-delicious-food.com/wp-content/uploads/2018/10/breakfast-board-500x500.jpg",
            "PNG",
            getStringFromResource(R.string.breakfast),
            "Italian",
            45,
            1,
            "Breakfast",
            getDateInstance().toString()
        )
        val lunch = MealListItem(
            2,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRoJBTc9RI3gHAWFG3-dRApc8TbPbvCPy4oS4gf6hcDTrEbK7L_pNUkyXXpDxt_9GZcFFQ&usqp=CAU",
            "PNG",
            getStringFromResource(R.string.lunch),
            "Iranian",
            36,
            2,
            "Lunch",
            getDateInstance().toString()
        )
        val dinner = MealListItem(
            3,
            "https://insanelygoodrecipes.com/wp-content/uploads/2021/03/Spaghetti-and-Meatballs-with-Tomato-Sauce-683x1024.png",
            "PNG",
            getStringFromResource(R.string.dinner),
            "Middle Eastern",
            65,
            1,
            "Dinner",
            getDateInstance().toString()
        )
        val snack = MealListItem(
            4,
            "https://img.freepik.com/free-vector/collection-delicious-snacks_23-2147914461.jpg",
            "PNG",
            getStringFromResource(R.string.snack),
            "Cuban",
            26,
            3,
            "Snack",
            getDateInstance().toString()
        )
        roomRepository.insertMeal(breakfast)
        roomRepository.insertMeal(lunch)
        roomRepository.insertMeal(dinner)
        roomRepository.insertMeal(snack)
    }

    val getFakeData  by lazyDeferred{
        roomRepository.getDayMeal(getDateInstance().toString())
    }
}