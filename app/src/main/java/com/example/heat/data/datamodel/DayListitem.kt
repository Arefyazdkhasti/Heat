package com.example.heat.data.datamodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//@Entity(tableName = "mealList_table")
@Parcelize
data class DayListItem(
    val dayPlan: ArrayList<MealListItem>
): Parcelable
