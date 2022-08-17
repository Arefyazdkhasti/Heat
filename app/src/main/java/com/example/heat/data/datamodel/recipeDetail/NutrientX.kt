package com.example.heat.data.datamodel.recipeDetail


import com.google.gson.annotations.SerializedName

data class NutrientX(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("percentOfDailyNeeds")
    val percentOfDailyNeeds: Double,
    @SerializedName("unit")
    val unit: String
)