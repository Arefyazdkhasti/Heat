package com.example.heat.util

import androidx.room.TypeConverter
import com.example.heat.util.enumerian.Disease
import com.example.heat.util.enumerian.IngredientAllergy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class MyConvertor {
    /*val gson = Gson()

    @TypeConverter
    fun recipeToString(recipe: String): String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun stringToRecipe(recipeString: String): String {
        val objectType = object : TypeToken<String>() {}.type
        return gson.fromJson(recipeString, objectType)
    }*/

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<String>{
        if (data == null) {
            return arrayListOf()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson<ArrayList<String>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<String>): String? {
        return gson.toJson(someObjects)
    }
        /* @TypeConverter
         fun toIngredientsAllergyType(value :String) = enumValueOf<IngredientAllergy>(value)

         @TypeConverter
         fun toDiseaseType(value :String) = enumValueOf<Disease>(value)
         @TypeConverter
         fun listToJson(value: ArrayList<String>?) = Gson().toJson(value)

         @TypeConverter
         fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
     */
}