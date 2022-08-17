package com.example.heat.data.datamodel.recipeDetail


import com.google.gson.annotations.SerializedName

data class WeightPerServing(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("unit")
    val unit: String
)