package com.example.heat.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.heat.data.datamodel.food.foodSummery.Calorie
import com.example.heat.data.datamodel.food.foodSummery.Carbohydrates
import com.example.heat.data.datamodel.food.foodSummery.Fat
import com.example.heat.data.datamodel.food.foodSummery.Protein
import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.Disease
import com.example.heat.util.enumerian.IngredientAllergy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


public class MyConvertor {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<String> {
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
}