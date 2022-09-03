package com.example.heat.ui.trackFood

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.util.UiUtils.Companion.getCurrentDate
import com.example.heat.util.lazyDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class TrackFoodsViewModel(
    private val heatRepository: HeatRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {



    val getDayMeals by lazyDeferred {
        roomRepository.getDayMeal(getCurrentDate())
    }
    val getWeekMeals by lazyDeferred {
        roomRepository.getWeekMeal()
    }

    fun eatOrUnEatFoodToRoom(mealItem: FoodSummery, eat: Boolean) {
        GlobalScope.launch {
            roomRepository.eatMeal(mealItem, eat)
        }
    }

}