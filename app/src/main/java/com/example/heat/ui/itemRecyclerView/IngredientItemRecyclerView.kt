package com.example.heat.ui.itemRecyclerView

import com.bumptech.glide.Glide
import com.example.heat.R
import com.example.heat.data.data.INGREDIENT_IMAGE_BASE_URL
import com.example.heat.data.datamodel.recipeDetail.ExtendedIngredient
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.IngredientItemBinding
import com.example.heat.databinding.RecipeItemBinding
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.setImageWithGlideWithView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class IngredientItemRecyclerView(val ingredient: ExtendedIngredient) : Item<GroupieViewHolder>() {


    override fun getLayout(): Int = R.layout.ingredient_item
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = IngredientItemBinding.bind(viewHolder.itemView)

        setImageWithGlideWithView(viewHolder.itemView, "$INGREDIENT_IMAGE_BASE_URL/${ingredient.image}", binding.ingredientImage)

        binding.apply {
            ingredientImage.clipToOutline = true
            ingredientName.text = ingredient.name
            ingredientUnit.text = ingredient.originalName
        }

    }
}