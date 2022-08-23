package com.example.heat.ui.itemRecyclerView

import android.view.View
import com.example.heat.R
import com.example.heat.data.datamodel.EatenMealItem
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.databinding.MealItemBinding
import com.example.heat.util.SendEvent
import com.example.heat.util.UiUtils.Companion.setImageWithGlideWithView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.lang.IllegalArgumentException


class MealItemRecyclerView(val mealItem: MealListItem, val sendEvent: SendEvent) :
    Item<GroupieViewHolder>() {
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


            mealCheckBox.isChecked = mealItem.eaten
            mealRefresh.visibility = if(mealItem.eaten) View.GONE else View.VISIBLE
            mealCheckBox.setOnCheckedChangeListener { compoundButton, checked ->
                sendEvent.sendCheckedStatus(checked, mealItem)

                if (checked)
                    mealRefresh.visibility = View.INVISIBLE
                else
                    mealRefresh.visibility = View.VISIBLE
            }
        }
    }

}