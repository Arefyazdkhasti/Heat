package com.example.heat.ui.itemRecyclerView

import android.annotation.SuppressLint
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
            progressBarNutrition.progress = nutrition.percentage
            progressPercentageNutrition.text = "${nutrition.percentage}%"
        }

    }
}