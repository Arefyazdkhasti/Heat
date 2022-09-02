package com.example.heat.data.network.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.SearchQuery
import com.example.heat.data.datamodel.food.IsLiked
import com.example.heat.data.datamodel.food.LikeStatus
import com.example.heat.data.datamodel.food.foodDetail.FoodDetail
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.data.datamodel.user.LoginRequest
import com.example.heat.data.datamodel.user.RegisterRequest
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.datamodel.user.UserRelatedResponse

interface HeatRepository {

    suspend fun getFoodDetail(id :Int) : LiveData<FoodDetail>

    suspend fun searchFoods(searchQuery: SearchQuery): LiveData<List<FoodSummery>>

    suspend fun login(userLogin: LoginRequest): LiveData<UserRelatedResponse>

    suspend fun register(userRegister: RegisterRequest): LiveData<UserRelatedResponse>

    suspend fun saveUserPreferences(userPreferences: UserPreferences): LiveData<UserRelatedResponse>

    suspend fun getUserPreferences(id: Int): LiveData<UserPreferences>

    suspend fun generatePlan(userID:Int, day:Int) : LiveData<List<DayListItem>>

    suspend fun getUserLikedFoods(id: Int): LiveData<List<FoodSummery>>

    suspend fun likeFood(userID: Int, foodID: Int) : LiveData<LikeStatus>

    suspend fun isFoodLiked(userID: Int, foodID: Int): LiveData<IsLiked>
}