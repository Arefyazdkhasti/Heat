package com.example.heat.data.datamodel.user

import android.os.Parcelable
import com.example.heat.util.enumerian.*
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
    var ingredientsAllergy: ArrayList<IngredientAllergy>,
    var disease: ArrayList<Disease>
): Parcelable
