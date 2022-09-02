package com.example.heat.data.datamodel.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.heat.util.enumerian.*
import kotlinx.parcelize.Parcelize

@Entity(tableName = "userPreference_table")
@Parcelize
data class UserPreferences(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    var name: String,
    var weight: Double,
    var height: Double,
    var age: Int,
    var gender: Gender,
    var activeLevel: ActiveLevel,
    var abstractGoal: AbstractGoal,
    var dietType: UserDietType,
    var ingredientsAllergy: ArrayList<String>,
    var disease: ArrayList<String>
): Parcelable
