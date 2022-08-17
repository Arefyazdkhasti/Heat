package com.example.heat.data.datamodel.recipeDetail


import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("equipment")
    val equipment: List<Equipment>,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>,
    @SerializedName("number")
    val number:Double,
    @SerializedName("step")
    val step: String
)