package com.example.heat.data.datamodel.user


import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)