package com.example.heat.ui.itemRecyclerView

import android.os.Build
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.databinding.ItemDayPlanBinding
import com.example.heat.ui.trackFood.TrackFoodsFragmentDirections
import com.example.heat.util.SendEvent
import com.example.heat.util.UiUtils.Companion.formatDate
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.lang.IllegalArgumentException


class DayPlanItemRecyclerView(val list: DayListItem, val sendEvent: SendEvent) :
    Item<GroupieViewHolder>(), SendEvent {
    private lateinit var binding: ItemDayPlanBinding
    private var unCheckOne = false
    override fun getLayout(): Int = R.layout.item_day_plan
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        binding = ItemDayPlanBinding.bind(viewHolder.itemView)

        binding.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //dayPlanDate.text = formatDate(list.dayPlan[0].date)
            }
            dayPlanDate.text = list.dayPlan[0].date

            if (list.dayPlan[0].eaten and list.dayPlan[1].eaten and list.dayPlan[2].eaten and list.dayPlan[3].eaten) {
                checkboxTickAllDay.isChecked = true
                refreshWholeDayPlan.visibility = View.INVISIBLE
            }

            checkboxTickAllDay.setOnCheckedChangeListener { compoundButton, checked ->
                sendEvent.sendWholeCheckedStatus(checked, list)
                if (checked) {
                    refreshWholeDayPlan.visibility = View.INVISIBLE
                    for (element in list.dayPlan) {
                        element.eaten = true
                    }
                } else {
                    if (!unCheckOne) {
                        refreshWholeDayPlan.visibility = View.VISIBLE
                        for (element in list.dayPlan) {
                            element.eaten = false
                        }
                    }
                }
                initRecyclerView(dayPlanRecyclerView, list.dayPlan)
                unCheckOne = false
            }

            initRecyclerView(dayPlanRecyclerView, list.dayPlan)
        }
    }

    private fun initRecyclerView(
        recyclerView: ShimmerRecyclerView,
        data: List<MealListItem>
    ) {

        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toRecipeListItems())
        }

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? MealItemRecyclerView)?.let {
                //TODO un commnet when real data come
                /*val navigate = TrackFoodsFragmentDirections.actionTrackFoodsFragmentToRecipeDetailFragment(it.mealItem.id)
                    Navigation.findNavController(view).navigate(navigate)*/
            }
        }

    }

    private fun List<MealListItem>.toRecipeListItems(): List<MealItemRecyclerView> = this.map {
        MealItemRecyclerView(it, this@DayPlanItemRecyclerView)
    }

    override fun sendCheckedStatus(check: Boolean, meal: MealListItem) {
        sendEvent.sendCheckedStatus(check, meal)
    }

    override fun sendWholeCheckedStatus(check: Boolean, day: DayListItem) {
    }

    override fun sendOneMealUnChecked() {
        // binding.refreshWholeDayPlan.visibility = View.VISIBLE
        // binding.checkboxTickAllDay.isChecked = false
        // unCheckOne = true
    }
}