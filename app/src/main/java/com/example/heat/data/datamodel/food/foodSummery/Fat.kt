package com.example.heat.data.datamodel.food.foodSummery


import com.google.gson.annotations.SerializedName

data class Fat(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("unit")
    val unit: String
)