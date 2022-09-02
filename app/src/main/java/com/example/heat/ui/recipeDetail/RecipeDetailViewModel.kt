package com.example.heat.ui.recipeDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.util.lazyDeferred

class RecipeDetailViewModel(
    private val recipeID: Int,
    private val heatRepository: HeatRepository
) : ViewModel() {

    private val userID = MutableLiveData(0)

    fun setUserID(id: Int) {
        userID.postValue(id)
    }


    val likeFood by lazyDeferred {
        (userID.value?.let { userID ->
            heatRepository.likeFood(userID, recipeID)
        })
    }

    val isFoodLiked by lazyDeferred {
        userID.value?.let { userID->
            heatRepository.isFoodLiked(userID, recipeID)
        }
    }

    val recipeDetail by lazyDeferred {
        heatRepository.getFoodDetail(recipeID)
    }




    sealed class RecipeDetailEventListener {
        data class LikeAndUnlikeRecipe(val recipeID: Int, val userID: Int) :
            RecipeDetailEventListener()
    }
}