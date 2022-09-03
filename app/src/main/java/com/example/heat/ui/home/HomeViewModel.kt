package com.example.heat.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.heat.R
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.food.foodSummery.*
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.util.UiUtils.Companion.getCurrentDate
import com.example.heat.util.UiUtils.Companion.getURLForResource
import com.example.heat.util.lazyDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val heatRepository: HeatRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    fun generateDayFakeData(): List<FoodSummery> {


        val breakfast = FoodSummery(
            Calorie(0.0, 0, "", ""),
            Carbohydrates(0.0, 0, "", ""),
            listOf(),
            listOf(),
            false,
            Fat(0.0, 0, "", ""),
            0,
            getURLForResource(R.drawable.breakfast),
            getCurrentDate(),
            listOf(),
            Protein(0.0, 0, "", ""),
            "",
            "Breakfast",
            ""
        )
        val lunch = FoodSummery(
            Calorie(0.0, 0, "", ""),
            Carbohydrates(0.0, 0, "", ""),
            listOf(),
            listOf(),
            false,
            Fat(0.0, 0, "", ""),
            1,
            getURLForResource(R.drawable.lunch),
            getCurrentDate(),
            listOf(),
            Protein(0.0, 0, "", ""),
            "",
            "Lunch",
            ""
        )
        val dinner = FoodSummery(
            Calorie(0.0, 0, "", ""),
            Carbohydrates(0.0, 0, "", ""),
            listOf(),
            listOf(),
            false,
            Fat(0.0, 0, "", ""),
            2,
            getURLForResource(R.drawable.dinner),
            getCurrentDate(),
            listOf(),
            Protein(0.0, 0, "", ""),
            "",
            "Dinner",
            ""
        )
        val snack = FoodSummery(
            Calorie(0.0, 0, "", ""),
            Carbohydrates(0.0, 0, "", ""),
            listOf(),
            listOf(),
            false,
            Fat(0.0, 0, "", ""),
            3,
            getURLForResource(R.drawable.snack),
            getCurrentDate(),
            listOf(),
            Protein(0.0, 0, "", ""),
            "",
            "Snack",
            ""
        )

        return listOf(breakfast, lunch, dinner, snack)
    }

    private val user = MutableLiveData(0)
    private val numberOfDays = MutableLiveData(1)

    fun setUserID(userID: Int) {
        user.postValue(userID)
    }

    fun setNumberOfDays(days: Int) {
        numberOfDays.postValue(days)
    }

    val generatePlanRequest by lazyDeferred {
        user.value?.let {
            numberOfDays.value?.let { days ->
                heatRepository.generatePlan(it, days)
            }
        }
    }

    fun saveFoodToRoom(list: List<DayListItem>) = GlobalScope.launch {
        for (item in list) {
            roomRepository.insertMeal(item.breakFast)
            roomRepository.insertMeal(item.dinner)
            roomRepository.insertMeal(item.lunch)
            roomRepository.insertMeal(item.snack)
        }
    }

    val mealDbSize by lazyDeferred {
        roomRepository.eatMealDBSize()
    }

    val userDayMeal by lazyDeferred {
        roomRepository.getDayMeal(getCurrentDate())
    }

    val userWeekMeals by lazyDeferred {
        roomRepository.getWeekMeal()
    }

    fun deletePreviousRecords(day:String) = GlobalScope.launch {
        roomRepository.deletePreviousRecords(day)
    }

    val getUserPreferenceDbSize by lazyDeferred{
        roomRepository.userPreferenceSize()
    }

    val getUserPreferences by lazyDeferred {
        user.value?.let {
            heatRepository.getUserPreferences(it)
        }
    }
    fun saveUserPreferences(userPreferences: UserPreferences) = viewModelScope.launch {
        roomRepository.insertUserPreferences(userPreferences)
    }
}