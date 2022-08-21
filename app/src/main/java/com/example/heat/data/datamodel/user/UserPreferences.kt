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
    val username: String,
    val weight: Double,
    val height: Double,
    val age: Int,
    val gender: Gender,
    val activeLevel: ActiveLevel,
    val abstractGoal: AbstractGoal,
    val dietType: DietType,
    val ingredientsAllergy: List<String>,
    val disease: List<String>
): Parcelable
