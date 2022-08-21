package com.example.heat.data.datamodel

import com.google.gson.annotations.SerializedName

//TODO should change due to the back-end

data class MealListItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("cuisine")
    val cuisine: String,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @SerializedName("servings")
    val servings: Int,
    @SerializedName("type")
    val type: String
)
