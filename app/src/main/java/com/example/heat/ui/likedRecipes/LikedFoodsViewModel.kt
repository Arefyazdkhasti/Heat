package com.example.heat.ui.likedRecipes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.util.ErrorHandling
import com.example.heat.util.lazyDeferred

class LikedFoodsViewModel (
    private val errorHandling: ErrorHandling,
    private val heatRepository: HeatRepository
) : ViewModel() {

    private val user = MutableLiveData(0)

    fun setUserID(userID: Int) {
        user.postValue(userID)
    }

    val likedRecipesList by lazyDeferred{
        user.value?.let { heatRepository.getUserLikedFoods(it,errorHandling) }
    }
}