package com.example.heat.data.network.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.network.response.NetworkDataSource
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

class HeatRepositoryImpl(
    private val networkDataSource: NetworkDataSource
) : HeatRepository {
    override suspend fun getFoodDetail(id: Int): LiveData<FoodDetail> {
        fetchFoodDetails(id)
        return networkDataSource.foodDetail
    }
    private suspend fun fetchFoodDetails(
        id: Int
    ) {
        networkDataSource.fetchFoodDetail(id)
    }

    override suspend fun searchFoods(searchQuery: SearchQuery): LiveData<List<FoodSummery>> {
        fetchSearchFoods(searchQuery)
        return networkDataSource.searchFood
    }
    private suspend fun fetchSearchFoods(
        searchQuery: SearchQuery
    ) {
        networkDataSource.fetchSearchFood(searchQuery)
    }

    override suspend fun login(userLogin: LoginRequest): LiveData<UserRelatedResponse> {
        fetchLogin(userLogin)
        return networkDataSource.login
    }
    private suspend fun fetchLogin(
        userLogin: LoginRequest
    ) {
        networkDataSource.loginUser(userLogin)
    }

    override suspend fun register(userRegister: RegisterRequest): LiveData<UserRelatedResponse> {
        fetchRegister(userRegister)
        return networkDataSource.register
    }
    private suspend fun fetchRegister(
        userRegister: RegisterRequest
    ) {
        networkDataSource.registerUser(userRegister)
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences): LiveData<UserRelatedResponse> {
        fetchSaveUserPreference(userPreferences)
        return networkDataSource.saveUserPreferences
    }
    private suspend fun fetchSaveUserPreference(
        userPreferences: UserPreferences
    ) {
        networkDataSource.saveUserPreferences(userPreferences)
    }

    override suspend fun getUserPreferences(id: Int): LiveData<UserPreferences> {
        fetchGetUserPreference(id)
        return networkDataSource.getUserPreferences
    }
    private suspend fun fetchGetUserPreference(
        id: Int
    ) {
        networkDataSource.fetchUserPreferences(id)
    }

    override suspend fun generatePlan(userID: Int, day: Int): LiveData<List<DayListItem>> {
        fetchGeneratePlan(userID,day)
        return networkDataSource.generateUserPlan
    }
    private suspend fun fetchGeneratePlan(
        userID: Int, day: Int
    ) {
        networkDataSource.generateUserPlan(userID,day)
    }

    override suspend fun getUserLikedFoods(id: Int): LiveData<List<FoodSummery>> {
        fetchUserLikedFoods(id)
        return networkDataSource.getUserLikedFoods
    }
    private suspend fun fetchUserLikedFoods(
        id: Int
    ) {
        networkDataSource.fetchUserLikedFoods(id)
    }

    override suspend fun likeFood(userID: Int, foodID: Int): LiveData<LikeStatus> {
        fetchLikeFood(userID,foodID)
        return networkDataSource.likeFood
    }
    private suspend fun fetchLikeFood(
        userID: Int, foodID: Int
    ) {
        networkDataSource.sendLikeRequest(userID,foodID)
    }

    override suspend fun isFoodLiked(userID: Int, foodID: Int): LiveData<IsLiked> {
        fetchIsFoodLiked(userID,foodID)
        return networkDataSource.isFoodLiked
    }
    private suspend fun fetchIsFoodLiked(
        userID: Int, foodID: Int
    ) {
        networkDataSource.fetchIsFoodLiked(userID,foodID)
    }
}