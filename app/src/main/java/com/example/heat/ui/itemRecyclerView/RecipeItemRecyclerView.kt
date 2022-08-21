package com.example.heat.ui.itemRecyclerView

import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.example.heat.R
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.RecipeItemBinding
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.setImageWithGlideWithView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class RecipeItemRecyclerView(val recipeItem: RecipeListItem) : Item<GroupieViewHolder>() {


    override fun getLayout(): Int = R.layout.recipe_item
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = RecipeItemBinding.bind(viewHolder.itemView)

        setImageWithGlideWithView(viewHolder.itemView, recipeItem.image, binding.recipeImage)
        binding.apply {
            recipeImage.clipToOutline = true
            recipeName.text = recipeItem.title

            recipeRoot.animation = AnimationUtils.loadAnimation(viewHolder.itemView.context, R.anim.recycler_item_anim)
        }

    }
}