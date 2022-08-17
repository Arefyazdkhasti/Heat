package com.example.heat.data.datamodel.recipeList


import com.google.gson.annotations.SerializedName

data class RecipeList(
    @SerializedName("number")
    val number: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("results")
    val results: List<RecipeListItem>,
    @SerializedName("totalResults")
    val totalResults: Int
)