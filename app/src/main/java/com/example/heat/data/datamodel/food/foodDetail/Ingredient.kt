package com.example.heat.data.datamodel.food.foodDetail


import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("aisle")
    val aisle: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageURL")
    val imageURL: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String
)