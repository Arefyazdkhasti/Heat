package com.example.heat.ui.itemRecyclerView

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.RecipeItemHorizontalBinding
import com.example.heat.util.UiUtils.Companion.setImageWithGlideWithView
import com.example.heat.util.enumerian.RecipeViewType
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item



class RecipeItemRecyclerView(val recipeItem: RecipeListItem, val type: RecipeViewType) :
    Item<GroupieViewHolder>() {

    override fun getLayout(): Int =
        if (type == RecipeViewType.LARGE) R.layout.recipe_item_horizontal else R.layout.recipe_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = RecipeItemHorizontalBinding.bind(viewHolder.itemView)

        setImageWithGlideWithView(viewHolder.itemView, recipeItem.image, binding.recipeImage)
        binding.apply {
            recipeImage.clipToOutline = true
            recipeName.text = recipeItem.title

            recipeRoot.animation =
                AnimationUtils.loadAnimation(viewHolder.itemView.context, R.anim.recycler_item_anim)
        }

    }
}