package com.example.heat.data.datamodel.recipeDetail


import com.google.gson.annotations.SerializedName

data class IngredientX(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("nutrients")
    val nutrients: List<Nutrient>,
    @SerializedName("unit")
    val unit: String
)