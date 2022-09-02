package com.example.heat.data.datamodel.food.foodDetail


import com.google.gson.annotations.SerializedName

data class FoodDetail(
    @SerializedName("cuisines")
    val cuisines: List<String>,
    @SerializedName("dietTypes")
    val dietTypes: List<String>,
    @SerializedName("imageLink")
    val imageLink: String,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>,
    @SerializedName("instructionSteps")
    val instructionSteps: List<InstructionStep>,
    @SerializedName("mealTypes")
    val mealTypes: List<String>,
    @SerializedName("nutrients")
    val nutrients: List<Nutrient>,
    @SerializedName("readyInMinute")
    val readyInMinute: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean
)