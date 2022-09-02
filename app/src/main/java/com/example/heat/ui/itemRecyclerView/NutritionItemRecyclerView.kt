package com.example.heat.ui.itemRecyclerView

import com.example.heat.R
import com.example.heat.data.datamodel.food.foodDetail.Nutrient
import com.example.heat.databinding.NutritionItemBinding
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NutritionItemRecyclerView(val nutrient: Nutrient) : Item<GroupieViewHolder>() {


    override fun getLayout(): Int = R.layout.nutrition_item
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = NutritionItemBinding.bind(viewHolder.itemView)


        binding.apply {
            val formattedNutrient = "${nutrient.name} : ${nutrient.amount}${nutrient.unit}"

            nutritionItemText.text = formattedNutrient
        }

    }
}