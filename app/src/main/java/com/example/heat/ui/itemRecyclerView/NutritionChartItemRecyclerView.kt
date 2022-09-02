package com.example.heat.ui.itemRecyclerView

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.heat.R
import com.example.heat.data.datamodel.NutritionType
import com.example.heat.databinding.NutritionChartViewBinding
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NutritionChartItemRecyclerView(val nutrition: NutritionType) : Item<GroupieViewHolder>() {


    override fun getLayout(): Int = R.layout.nutrition_chart_view

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = NutritionChartViewBinding.bind(viewHolder.itemView)


        binding.apply {
            chartViewNutritionName.text = nutrition.name
            chartViewNutritionIcon.setImageResource(nutrition.icon)
            progressPercentageNutrition.text = "${nutrition.percentage}%"
            when (nutrition.name) {
                "Calories" ->{
                    progressBarNutritionCalorie.visibility = View.VISIBLE
                    progressBarNutritionCalorie.progress = nutrition.percentage
                    progressPercentageNutrition.setTextColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.calories_green))
                }
                "Protein" ->{
                    progressBarNutritionProtein.visibility = View.VISIBLE
                    progressBarNutritionProtein.progress = nutrition.percentage
                    progressPercentageNutrition.setTextColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.protein_blue))
                }
                "Fat" ->{
                    progressBarNutritionFat.visibility = View.VISIBLE
                    progressBarNutritionFat.progress = nutrition.percentage
                    progressPercentageNutrition.setTextColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.fat_yellow))
                }
                "Carbo" ->{
                    progressBarNutritionCarbo.visibility = View.VISIBLE
                    progressBarNutritionCarbo.progress = nutrition.percentage
                    progressPercentageNutrition.setTextColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.carbo_red))
                }
                else ->{
                    progressBarNutritionCalorie.visibility = View.VISIBLE
                    progressBarNutritionCalorie.progress = nutrition.percentage
                    progressPercentageNutrition.setTextColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.calories_green))
                }
            }
        }

    }
}