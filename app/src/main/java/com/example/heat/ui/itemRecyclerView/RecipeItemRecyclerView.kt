package com.example.heat.ui.itemRecyclerView

import com.bumptech.glide.Glide
import com.example.heat.R
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.RecipeItemBinding
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class RecipeItemRecyclerView(val recipeItem: RecipeListItem): Item<GroupieViewHolder>() {


    override fun getLayout(): Int = R.layout.recipe_item
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = RecipeItemBinding.bind(viewHolder.itemView)

        Glide.with(viewHolder.itemView)
            .load(recipeItem.image)
            .placeholder(R.drawable.load)
            .into(binding.recipeImage)

        binding.recipeImage.clipToOutline = true
        binding.recipeName.text = recipeItem.title

    }
}