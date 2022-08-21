package com.example.heat.ui.itemRecyclerView

import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.example.heat.R
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.MealItemBinding
import com.example.heat.databinding.RecipeItemBinding
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.setImageWithGlideWithView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MealItemRecyclerView(val mealItem: MealListItem) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int = R.layout.meal_item
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = MealItemBinding.bind(viewHolder.itemView)

        setImageWithGlideWithView(viewHolder.itemView, mealItem.image, binding.mealImage)
        binding.apply {
            mealImage.clipToOutline = true
            mealName.text = mealItem.title
            mealType.text = mealItem.type
            mealCuisine.text = mealItem.cuisine
            val detail = "${mealItem.readyInMinutes}min    ${mealItem.servings} serving"
            mealDetail.text = detail
        }
    }
}