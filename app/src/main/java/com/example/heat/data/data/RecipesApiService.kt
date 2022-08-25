package com.example.heat.data.data

import com.example.heat.data.datamodel.recipeDetail.Recipe
import com.example.heat.data.datamodel.recipeList.RecipeList
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


const val API_KEY = "11f787419de348f8bad5732e9eb1f064"
const val BASE_URL = "https://api.spoonacular.com/"
const val INGREDIENT_IMAGE_BASE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

interface RecipesApiService {


    //https://api.spoonacular.com/recipes/complexSearch?apiKey=9c9d4b13164f4c0299176f4db1bd95e9&type=breakfast
    @GET("/recipes/complexSearch")
    fun getRecipesListAsync(
        @Query("type") type: String? ,
        @Query("offset") offset: Int? ,
    ): Deferred<RecipeList>

    //https://api.spoonacular.com/recipes/647043/information?apiKey=9c9d4b13164f4c0299176f4db1bd95e9&includeNutrition=true
    @GET("/recipes/{id}/information")
    fun getRecipesDetailAsync(
        @Path("id")id: Int,
        @Query("includeNutrition") includeNutrition:Boolean = true,
    ): Deferred<Recipe>

    //https://api.spoonacular.com/recipes/complexSearch?apiKey=9c9d4b13164f4c0299176f4db1bd95e9&type=breakfast&qyuery=egg&diet=vegan
    @GET("/recipes/complexSearch")
    fun searchRecipesAsync(
        @Query("query") query: String? ,
        @Query("type") type: String? ,
        @Query("diet") diet: String? ,
        @Query("offset") offset: Int? ,
        @Query("number") number: Int? ,
    ): Deferred<RecipeList>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): RecipesApiService {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("apiKey", API_KEY)
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

            /*val gson = GsonBuilder()
                .setLenient()
                .create()*/

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecipesApiService::class.java)
        }
    }
}