package com.example.heat.data.datamodel

import com.example.heat.util.enum.DietType
import com.example.heat.util.enum.MealType

data class SearchQuery(
    var query: String,
    var dietType: DietType,
    var mealType: MealType
)
