package com.example.heat.ui.itemRecyclerView

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.databinding.ItemDayPlanBinding
import com.example.heat.ui.trackFood.TrackFoodsFragmentDirections
import com.example.heat.util.SendEvent
import com.example.heat.util.UiUtils.Companion.showToast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class DayPlanItemRecyclerView(val list: DayListItem, val sendEvent: SendEvent) :
    Item<GroupieViewHolder>(), SendEvent {

    private lateinit var binding: ItemDayPlanBinding
    override fun getLayout(): Int = R.layout.item_day_plan
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        binding = ItemDayPlanBinding.bind(viewHolder.itemView)

        binding.apply {
            dayPlanDate.text = list.breakFast.localDate.toString()

            if (list.breakFast.eaten and list.lunch.eaten and list.dinner.eaten and list.snack.eaten) {
                checkboxTickAllDay.isChecked = true
                refreshWholeDayPlan.visibility = View.INVISIBLE
            }

            refreshWholeDayPlan.setOnClickListener {
                sendEvent.regenerateWholePlan(list)
            }
            checkboxTickAllDay.setOnCheckedChangeListener { compoundButton, checked ->
                if(!compoundButton.isPressed) {
                    return@setOnCheckedChangeListener;
                }
                sendEvent.sendWholeCheckedStatus(checked, list)
                if (checked) {
                    refreshWholeDayPlan.visibility = View.INVISIBLE
                    list.apply {
                        breakFast.eaten = true
                        lunch.eaten = true
                        dinner.eaten = true
                        snack.eaten = true
                    }
                } else {
                    refreshWholeDayPlan.visibility = View.VISIBLE
                    list.apply {
                        breakFast.eaten = false
                        lunch.eaten = false
                        dinner.eaten = false
                        snack.eaten = false
                    }
                }
                initRecyclerView(dayPlanRecyclerView, list)
            }

            initRecyclerView(dayPlanRecyclerView, list)
        }
    }

    private fun initRecyclerView(
        recyclerView: ShimmerRecyclerView,
        data: DayListItem,
    ) {
        val listOfFoodSummery = listOf(data.breakFast, data.lunch, data.dinner, data.snack)
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(listOfFoodSummery.toRecipeListItems())
        }

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? MealItemRecyclerView)?.let {
                val navigate =
                    TrackFoodsFragmentDirections.actionTrackFoodsFragmentToRecipeDetailFragment(it.mealItem.id)
                Navigation.findNavController(view).navigate(navigate)
            }
        }

    }

    private fun List<FoodSummery>.toRecipeListItems(): List<MealItemRecyclerView> = this.map {
        MealItemRecyclerView(it, this@DayPlanItemRecyclerView)
    }

    override fun sendCheckedStatus(check: Boolean, meal: FoodSummery) {
        sendEvent.sendCheckedStatus(check, meal)
    }

    override fun sendWholeCheckedStatus(check: Boolean, day: DayListItem) {
    }

    override fun sendOneMealUnChecked() {
        binding.checkboxTickAllDay.isChecked = false
    }

    override fun regenerateOneMeal(meal: FoodSummery) {
        sendEvent.regenerateOneMealToHome(meal)
    }

    override fun regenerateWholePlan(plan: DayListItem) {

    }

    override fun regenerateOneMealToHome(meal: FoodSummery) {

    }
}