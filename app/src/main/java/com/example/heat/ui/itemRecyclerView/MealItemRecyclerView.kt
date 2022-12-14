package com.example.heat.ui.itemRecyclerView

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.lifecycle.asLiveData
import com.example.heat.R
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.databinding.MealItemBinding
import com.example.heat.util.SendEvent
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.CHECK_MEAL
import com.example.heat.util.UiUtils.Companion.CHECK_MEAL_GUIDE_SHOLD_SHOW_KEY
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UiUtils.Companion.setImageWithGlideWithView
import com.example.heat.util.manager.ShowCaseManager
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MealItemRecyclerView(val mealItem: FoodSummery, val sendEvent: SendEvent) :
    Item<GroupieViewHolder>() {
    override fun getLayout(): Int = R.layout.meal_item
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = MealItemBinding.bind(viewHolder.itemView)
        setImageWithGlideWithView(viewHolder.itemView, mealItem.imageLink, binding.mealImage)
        binding.apply {
            mealImage.clipToOutline = true
            mealName.text = mealItem.title

            mealType.text = mealItem.mealLabel

            var cuisines = ""
            for (item in mealItem.cuisines) {
                cuisines += "$item "
            }
            mealCuisine.text = cuisines

            val detail = "${mealItem.readyInMinute}min    ${mealItem.calorie.amount} kcal"
            mealDetail.text = detail


            mealCheckBox.isChecked = mealItem.eaten
            mealRefresh.visibility = if(mealItem.eaten) View.GONE else View.VISIBLE
            mealCheckBox.setOnCheckedChangeListener { compoundButton, checked ->
                sendEvent.sendCheckedStatus(checked, mealItem)

                if (checked)
                    mealRefresh.visibility = View.INVISIBLE
                else {
                    sendEvent.sendOneMealUnChecked()
                    mealRefresh.visibility = View.VISIBLE
                }
            }
            mealRefresh.setOnClickListener {
                sendEvent.regenerateOneMeal(mealItem)
            }
        }
    }
}