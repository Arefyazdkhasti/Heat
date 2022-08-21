package com.example.heat.data.datamodel.user

import android.os.Parcelable
import com.example.heat.util.enum.AbstractGoal
import com.example.heat.util.enum.ActiveLevel
import com.example.heat.util.enum.DietType
import com.example.heat.util.enum.Gender
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPreferences(
    val id: Int,
    var name: String,
    var weight: Double,
    var height: Double,
    var age: Int,
    var gender: Gender,
    var activeLevel: ActiveLevel,
    var abstractGoal: AbstractGoal,
    var dietType: DietType,
    var ingredientsAllergy: List<String>,
    var disease: List<String>
): Parcelable
