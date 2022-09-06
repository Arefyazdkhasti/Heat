package com.example.heat.data.network.response

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
import com.example.heat.util.ErrorHandling

interface NetworkDataSource {
/*

    //Recipes
    val recipesList: LiveData<RecipeList>
    suspend fun fetchRecipesList(offset: Int)

    val breakfastRecipesList: LiveData<RecipeList>
    suspend fun fetchBreakfastRecipesList(type:String, offset: Int)

    val snackRecipesList: LiveData<RecipeList>
    suspend fun fetchSnackRecipesList(type:String, offset: Int)

    val mainCourseRecipesList: LiveData<RecipeList>
    suspend fun fetchMainCourseRecipesList(type:String, offset: Int)

    val recipeDetail : LiveData<Recipe>
    suspend fun fetchRecipeDetail(id :Int)
*/

    //foods
    val foodDetail: LiveData<FoodDetail>
    suspend fun fetchFoodDetail(id: Int)

    //search
    val searchFood: LiveData<List<FoodSummery>>
    suspend fun fetchSearchFood(searchQuery: SearchQuery,  errorHandling: ErrorHandling)

    //login
    val login: LiveData<UserRelatedResponse>
    suspend fun loginUser(user: LoginRequest)

    //register
    val register: LiveData<UserRelatedResponse>
    suspend fun registerUser(user: RegisterRequest)

    //save UserPreferences
    val saveUserPreferences: LiveData<UserRelatedResponse>
    suspend fun saveUserPreferences(userPreferences: UserPreferences)

    //get UserPreferences
    val getUserPreferences: LiveData<UserPreferences>
    suspend fun fetchUserPreferences(id: Int)

    //generate user Plan
    val generateUserPlan: LiveData<List<DayListItem>>
    suspend fun generateUserPlan(userID: Int, day: Int)

    //get liked foods
    val getUserLikedFoods: LiveData<List<FoodSummery>>
    suspend fun fetchUserLikedFoods(id: Int, errorHandling: ErrorHandling)

    //like/unlike food
    val likeFood: LiveData<LikeStatus>
    suspend fun sendLikeRequest(userID: Int, foodID: Int)

    //is food previously liked
    val isFoodLiked: LiveData<IsLiked>
    suspend fun fetchIsFoodLiked(userID: Int, foodID: Int)

}