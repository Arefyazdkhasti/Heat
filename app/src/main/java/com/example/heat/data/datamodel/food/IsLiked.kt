package com.example.heat.data.datamodel.food


import com.google.gson.annotations.SerializedName

data class IsLiked(
    @SerializedName("isLiked")
    val isLiked: Boolean
)