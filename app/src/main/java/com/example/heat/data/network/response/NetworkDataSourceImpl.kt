package com.example.heat.data.network.response

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.heat.util.NoConnectivityException
import com.example.heat.data.network.HeatApiService
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
import retrofit2.HttpException

class NetworkDataSourceImpl(
    private val heatApiService: HeatApiService
) : NetworkDataSource {

    private val _foodDetail = MutableLiveData<FoodDetail>()
    override val foodDetail: LiveData<FoodDetail>
        get() = _foodDetail

    override suspend fun fetchFoodDetail(id: Int) {
        try {
            val fetchFood = heatApiService.getFoodDetailAsync(id).await()
            _foodDetail.postValue(fetchFood)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }


    private val _searchFood = MutableLiveData<List<FoodSummery>>()
    override val searchFood: LiveData<List<FoodSummery>>
        get() = _searchFood

    override suspend fun fetchSearchFood(searchQuery: SearchQuery) {
        try {
            val fetchSearchFoods = heatApiService.searchFoodsAsync(searchQuery).await()
            _searchFood.postValue(fetchSearchFoods)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }


    private val _login = MutableLiveData<UserRelatedResponse>()
    override val login: LiveData<UserRelatedResponse>
        get() = _login

    override suspend fun loginUser(user: LoginRequest) {
        try {
            val fetchLogin = heatApiService.loginAsync(user)
            /*when (fetchLogin.code()) {
                404 -> _login.postValue(UserRelatedResponse(404))
                406 -> _login.postValue(UserRelatedResponse(406))
                else ->
            }*/
            _login.postValue(fetchLogin.await())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        } catch (e: Exception) {
            when (e) {
                is NoConnectivityException -> Log.e("Connectivity", "No internet connection", e)
                is HttpException -> {
                    when (e.code()) {
                        404 -> _login.postValue(UserRelatedResponse(404))
                        406 -> _login.postValue(UserRelatedResponse(406))
                        else ->  _login.postValue(UserRelatedResponse(408))
                    }
                }
            }
        }
    }


    private val _register = MutableLiveData<UserRelatedResponse>()
    override val register: LiveData<UserRelatedResponse>
        get() = _register

    override suspend fun registerUser(user: RegisterRequest) {
        try {
            val fetchRegister = heatApiService.registerAsync(user).await()
            _register.postValue(fetchRegister)
        } catch (e: Exception) {
            when (e) {
                is NoConnectivityException -> Log.e("Connectivity", "No internet connection", e)
                is HttpException -> {
                    when (e.code()) {
                        409 -> _login.postValue(UserRelatedResponse(409))
                        else ->  _login.postValue(UserRelatedResponse(408))
                    }
                }
            }
        }
    }


    private val _saveUserPreferences = MutableLiveData<UserRelatedResponse>()
    override val saveUserPreferences: LiveData<UserRelatedResponse>
        get() = _saveUserPreferences

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        try {
            val fetchSaveUserPreference =
                heatApiService.saveUserPreferencesAsync(userPreferences).await()
            _saveUserPreferences.postValue(fetchSaveUserPreference)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }


    private val _getUserPreferences = MutableLiveData<UserPreferences>()
    override val getUserPreferences: LiveData<UserPreferences>
        get() = _getUserPreferences

    override suspend fun fetchUserPreferences(id: Int) {
        try {
            val fetchSaveUserPreference = heatApiService.getUserPreferencesAsync(id).await()
            _getUserPreferences.postValue(fetchSaveUserPreference)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }


    private val _getUserPlan = MutableLiveData<List<DayListItem>>()
    override val generateUserPlan: LiveData<List<DayListItem>>
        get() = _getUserPlan

    override suspend fun generateUserPlan(userID: Int, day: Int) {
        try {
            val fetchSaveUserPlan = heatApiService.generatePlanAsync(userID, day).await()
            _getUserPlan.postValue(fetchSaveUserPlan)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }


    private val _getUserLikedFoods = MutableLiveData<List<FoodSummery>>()
    override val getUserLikedFoods: LiveData<List<FoodSummery>>
        get() = _getUserLikedFoods


    override suspend fun fetchUserLikedFoods(id: Int) {
        try {
            val fetchUserLikedFoods = heatApiService.getLikedFoodsAsync(id).await()
            _getUserLikedFoods.postValue(fetchUserLikedFoods)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }


    private val _likeFood = MutableLiveData<LikeStatus>()
    override val likeFood: LiveData<LikeStatus>
        get() = _likeFood

    override suspend fun sendLikeRequest(userID: Int, foodID: Int) {
        try {
            val fetchLikeFood = heatApiService.likeFoodAsync(foodID, userID).await()
            _likeFood.postValue(fetchLikeFood)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }


    private val _isFoodLiked = MutableLiveData<IsLiked>()
    override val isFoodLiked: LiveData<IsLiked>
        get() = _isFoodLiked

    override suspend fun fetchIsFoodLiked(userID: Int, foodID: Int) {
        try {
            val fetchIsFoodLiked = heatApiService.getIsFoodLikedAsync(foodID, userID).await()
            _isFoodLiked.postValue(fetchIsFoodLiked)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}