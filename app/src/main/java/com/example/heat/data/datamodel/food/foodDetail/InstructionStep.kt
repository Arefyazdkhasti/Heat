package com.example.heat.data.datamodel.food.foodDetail


import com.google.gson.annotations.SerializedName

data class InstructionStep(
    @SerializedName("id")
    val id: Int,
    @SerializedName("number")
    val number: Int,
    @SerializedName("step")
    val step: String
)