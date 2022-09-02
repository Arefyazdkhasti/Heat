package com.example.heat.data.datamodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//@Entity(tableName = "mealList_table")
@Parcelize
data class DayListItem(
    var breakFast :FoodSummery,
    var lunch :FoodSummery,
    var dinner :FoodSummery,
    var snack :FoodSummery
): Parcelable
