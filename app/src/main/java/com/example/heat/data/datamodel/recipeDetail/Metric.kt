package com.example.heat.data.datamodel.recipeDetail


import com.google.gson.annotations.SerializedName

data class Metric(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("unitLong")
    val unitLong: String,
    @SerializedName("unitShort")
    val unitShort: String
)