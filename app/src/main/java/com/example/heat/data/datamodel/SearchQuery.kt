package com.example.heat.data.datamodel

import com.example.heat.util.enumerian.Cuisine
import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.MealType
import com.google.gson.annotations.SerializedName

data class SearchQuery(
    @SerializedName("cuisine")
    var cuisine: Cuisine,
    @SerializedName("dietType")
    var dietType: DietType,
    @SerializedName("keyword")
    var keyword: String,
    @SerializedName("maxCalorie")
    var maxCalorie: Int,
    @SerializedName("mealType")
    var mealType: MealType,
    @SerializedName("minCalorie")
    var minCalorie: Int,
)
