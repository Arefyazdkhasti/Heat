package com.example.heat.data.datamodel.food.foodSummery


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RawQuery
import androidx.room.TypeConverters
import com.example.heat.util.FoodSummeryConverter
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDate
import java.util.*

@TypeConverters(FoodSummeryConverter::class)
@Entity(tableName = "foodSummery_list_table")
@Parcelize
data class FoodSummery(
    @SerializedName("calorie")
    val calorie: @RawValue Calorie,
    @SerializedName("carbohydrates")
    val carbohydrates:@RawValue Carbohydrates,
    @SerializedName("cuisines")
    val cuisines: List<String>,
    @SerializedName("dietTypes")
    val dietTypes: List<String>,
    @SerializedName("eaten")
    var eaten: Boolean,
    @SerializedName("fat")
    val fat: @RawValue Fat,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageLink")
    val imageLink: String,
    @SerializedName("localDate")
    var localDate: String,
    @SerializedName("mealTypes")
    val mealTypes: List<String>,
    @SerializedName("protein")
    val protein: @RawValue Protein,
    @SerializedName("readyInMinute")
    val readyInMinute: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("mealLabel")
    var mealLabel: String,
):Parcelable