package com.example.heat.data.datamodel.recipeList


import com.google.gson.annotations.SerializedName

data class RecipeListItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("title")
    val title: String
)