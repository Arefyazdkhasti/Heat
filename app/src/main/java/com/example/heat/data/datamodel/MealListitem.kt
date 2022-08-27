package com.example.heat.data.datamodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "mealList_table")
@Parcelize
data class MealListItem(
    @PrimaryKey(autoGenerate = false)
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
    val calories: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("eaten")
    var eaten: Boolean
): Parcelable
