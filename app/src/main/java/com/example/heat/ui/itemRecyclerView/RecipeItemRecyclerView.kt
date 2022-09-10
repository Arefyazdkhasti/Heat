package com.example.heat.ui.itemRecyclerView

import android.annotation.SuppressLint
import android.view.animation.AnimationUtils
import com.example.heat.R
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.databinding.RecipeItemHorizontalBinding
import com.example.heat.util.UiUtils.Companion.setImageWithGlideWithView
import com.example.heat.util.enumerian.RecipeViewType
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item



class RecipeItemRecyclerView(val foodSummery: FoodSummery, val type: RecipeViewType) :
    Item<GroupieViewHolder>() {

    override fun getLayout(): Int =
        if (type == RecipeViewType.LARGE) R.layout.recipe_item_horizontal else R.layout.recipe_item

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = RecipeItemHorizontalBinding.bind(viewHolder.itemView)

        setImageWithGlideWithView(viewHolder.itemView, foodSummery.imageLink, binding.recipeImage)
        binding.apply {
            recipeImage.clipToOutline = true
            recipeName.text = foodSummery.title
            recipeCal.text = "${foodSummery.calorie.amount} ${foodSummery.calorie.unit}"
            recipeRoot.animation =
                AnimationUtils.loadAnimation(viewHolder.itemView.context, R.anim.recycler_item_anim)
        }

    }
}