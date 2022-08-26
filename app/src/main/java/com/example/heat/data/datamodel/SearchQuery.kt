package com.example.heat.data.datamodel

import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.MealType

data class SearchQuery(
    var query: String,
    var dietType: DietType,
    var mealType: MealType,
    var minCalorie: Int,
    var maxCalorie: Int
)
