package com.example.heat.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.heat.data.datamodel.food.foodSummery.Calorie
import com.example.heat.data.datamodel.food.foodSummery.Carbohydrates
import com.example.heat.data.datamodel.food.foodSummery.Fat
import com.example.heat.data.datamodel.food.foodSummery.Protein
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class FoodSummeryConverter {

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

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromProtein(value: Protein): String {
        val gson = Gson()
        val type = object : TypeToken<Protein>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toProtein(value: String): Protein {
        val gson = Gson()
        val type = object : TypeToken<Protein>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromFat(value: Fat): String {
        val gson = Gson()
        val type = object : TypeToken<Fat>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toFat(value: String): Fat {
        val gson = Gson()
        val type = object : TypeToken<Fat>() {}.type
        return gson.fromJson(value, type)
    }


    @TypeConverter
    fun fromProtein(value: Carbohydrates): String {
        val gson = Gson()
        val type = object : TypeToken<Carbohydrates>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCarbohydrates(value: String): Carbohydrates {
        val gson = Gson()
        val type = object : TypeToken<Carbohydrates>() {}.type
        return gson.fromJson(value, type)
    }


    @TypeConverter
    fun fromCalories(value: Calorie): String {
        val gson = Gson()
        val type = object : TypeToken<Calorie>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCalories(value: String): Calorie {
        val gson = Gson()
        val type = object : TypeToken<Calorie>() {}.type
        return gson.fromJson(value, type)
    }
}