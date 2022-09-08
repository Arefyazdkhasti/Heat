package com.example.heat.data.network

import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.SearchQuery
import com.example.heat.data.datamodel.food.IsLiked
import com.example.heat.data.datamodel.food.LikeStatus
import com.example.heat.data.datamodel.user.UserRelatedResponse
import com.example.heat.data.datamodel.food.foodDetail.FoodDetail
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.data.datamodel.user.LoginRequest
import com.example.heat.data.datamodel.user.RegisterRequest
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.network.connection.ConnectivityInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.*


//const val API_KEY = "9c9d4b13164f4c0299176f4db1bd95e9"
//const val BASE_URL = "https://api.spoonacular.com/"
const val BASE_URL = "http://192.168.36.95:8000/"
const val INGREDIENT_IMAGE_BASE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

interface HeatApiService {

    //https://api.spoonacular.com/recipes/complexSearch?apiKey=9c9d4b13164f4c0299176f4db1bd95e9&type=breakfast
    /*@GET("/recipes/complexSearch")
    fun getRecipesListAsync(
        @Query("type") type: String? ,
        @Query("offset") offset: Int? ,
    ): Deferred<RecipeList>*/

    //https://api.spoonacular.com/recipes/647043/information?apiKey=9c9d4b13164f4c0299176f4db1bd95e9&includeNutrition=true
    //https://localhost:8080/search
    @GET("/food/{id}")
    fun getFoodDetailAsync(
        @Path("id")id: Int,
    ): Deferred<FoodDetail>

    //https://api.spoonacular.com/recipes/complexSearch?apiKey=9c9d4b13164f4c0299176f4db1bd95e9&type=breakfast&qyuery=egg&diet=vegan
    //https://localhost:8080/search
    @POST("/search")
    fun searchFoodsAsync(
        @Body searchRequestModel: SearchQuery?,
    ): Deferred<List<FoodSummery>>

    //https://localhost:8080/user/{id}/generatePlan/{day}
    @GET("/user/{id}/generatePlan/{day}")
    fun generatePlanAsync(
        @Path("id") userId:Int,
        @Path("day") day:Int
    ): Deferred<List<DayListItem>>

    //https://localhost:8080/user/{userId}/food/{foodId}/isLiked
    @GET("/user/{userId}/food/{foodId}/isLiked")
    fun getIsFoodLikedAsync(
        @Path("foodId") foodId:Int,
        @Path("userId") userId:Int,
    ): Deferred<IsLiked>

    //https://localhost:8080/user/{id}/foodLikes
    @GET("/user/{id}/foodLikes")
    fun getLikedFoodsAsync(
        @Path("id") id:Int,
    ): Deferred<List<FoodSummery>>

    //https://localhost:8080/user/{userId}/food/{foodId}/like
    @PATCH("/user/{userId}/food/{foodId}/like")
    fun likeFoodAsync(
        @Path("foodId") foodId:Int,
        @Path("userId") userId:Int,
    ): Deferred<LikeStatus>

    //https://localhost:8080/user/addInfo
    @POST("/user/addInfo")
    fun saveUserPreferencesAsync(
       @Body userPreferenceRequestModel: UserPreferences
    ):Deferred<UserRelatedResponse>

    //https://localhost:8080/user/{id}
    @GET("/user/{id}")
    fun getUserPreferencesAsync(
        @Path("id") id: Int
    ): Deferred<UserPreferences>

    //https://localhost:8080/user/login
    @POST("/user/login")
    fun loginAsync(
        @Body userLoginRequestModel: LoginRequest
    ): Deferred<UserRelatedResponse>

    //https://localhost:8080/user/register
    @POST("/user/register")
    fun registerAsync(
        @Body userRegisterRequestModel: RegisterRequest
    ): Deferred<UserRelatedResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): HeatApiService {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    //.addQueryParameter("apiKey", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HeatApiService::class.java)
        }
    }
}