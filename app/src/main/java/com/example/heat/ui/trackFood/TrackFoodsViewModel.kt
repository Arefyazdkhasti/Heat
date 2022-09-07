package com.example.heat.ui.trackFood

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.ui.setting.disease.DiseaseViewModel
import com.example.heat.util.UiUtils.Companion.getCurrentDate
import com.example.heat.util.lazyDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val date = MutableLiveData("")

    fun setDate(d: String) {
        date.postValue(d)
    }

    val getSpecificDayMeal by lazyDeferred {
        date.value?.let { day ->
            roomRepository.getDayMeal(day)
        }
    }

    private val user = MutableLiveData(0)

    fun setUserID(userID: Int) {
        user.postValue(userID)
    }

    val generatePlanRequest by lazyDeferred {
        user.value?.let { user ->
            heatRepository.generatePlan(user, 1)
        }
    }

    fun saveNewlyGeneratedDayPlanToRoom(dateToRemove: String, item: DayListItem) =
        GlobalScope.launch {
            //remove old day plan
            roomRepository.deleteDayPlanByDate(dateToRemove)
            //add new day plan //TODO not add eaten meals?
            roomRepository.insertMeal(item.breakFast)
            roomRepository.insertMeal(item.dinner)
            roomRepository.insertMeal(item.lunch)
            roomRepository.insertMeal(item.snack)
        }

    private val transactionsEventChannel = Channel<TrackFoodsTransactionsEvents>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()

    fun showToast(msg: String) = viewModelScope.launch {
        transactionsEventChannel.send(
            TrackFoodsViewModel.TrackFoodsTransactionsEvents.ShowToastMessage(
                msg
            )
        )
    }

    sealed class TrackFoodsTransactionsEvents{
        data class ShowToastMessage(val msg: String): TrackFoodsTransactionsEvents()
    }
}