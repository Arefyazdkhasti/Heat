package com.example.heat.ui.trackFood

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.databinding.FragmentTrackFoodsBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.DayPlanItemRecyclerView
import com.example.heat.util.SendEvent
import com.example.heat.util.UiUtils.Companion.getDayOrWeekFromSetting
import com.example.heat.util.UiUtils.Companion.getUserIDFromDataStore
import com.example.heat.util.UiUtils.Companion.isNetworkConnected
import com.example.heat.util.UiUtils.Companion.showToast
import com.example.heat.util.UiUtils.Companion.stringFromResourcesByName
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@RequiresApi(Build.VERSION_CODES.O)
class TrackFoodsFragment : ScopedFragment(), KodeinAware, SendEvent {
    override val kodein by closestKodein()
    private val viewModelFactory: TrackFoodsViewModelFactory by instance()

    private lateinit var viewModel: TrackFoodsViewModel

    private var _binding: FragmentTrackFoodsBinding? = null
    private val binding
        get() = _binding!!

    private val requestList = arrayListOf<Pair<Boolean, FoodSummery>>()

    private var numberOfTimes = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        _binding = FragmentTrackFoodsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrackFoodsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
        binding.backArrow.setOnClickListener {
            for (item in requestList)
                viewModel.eatOrUnEatFoodToRoom(item.second, item.first)
            requireActivity().onBackPressed()
        }

        viewModel.setUserID(getUserIDFromDataStore(requireContext(), viewLifecycleOwner))

        loadDataFromRoom()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is TrackFoodsViewModel.TrackFoodsTransactionsEvents.ShowToastMessage -> {
                        showToast(requireContext(),event.msg)
                    }
                }
            }

        }
    }

    private fun loadDataFromRoom() = launch {
        when {
            getDayOrWeekFromSetting(requireContext()) == requireContext().stringFromResourcesByName(
                "one_week_plan"
            ) -> {
                viewModel.getWeekMeals.await().observe(viewLifecycleOwner, Observer {
                    val data: ArrayList<DayListItem> = arrayListOf()

                    var i = 0
                    var index = 0
                    while (i + 3 < it.size) {
                        val list: ArrayList<FoodSummery> = arrayListOf()

                        list.addAll(it.slice(i..i + 3))
                        data.add(DayListItem(list[0], list[1], list[2], list[3]))
                        i += 4
                        index++
                    }
                    initRecyclerView(binding.mealRecyclerView, data)
                })
            }
            else -> {
                viewModel.getDayMeals.await().observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            val data: ArrayList<DayListItem> = arrayListOf()
                            var i = 0
                            var index = 0
                            while (i + 3 < it.size) {
                                val list: ArrayList<FoodSummery> = arrayListOf()

                                list.addAll(it.slice(i..i + 3))
                                data.add(DayListItem(list[0], list[1], list[2], list[3]))
                                i += 4
                                index++
                            }
                            if (data.isNotEmpty())
                                initRecyclerView(binding.mealRecyclerView, listOf(data[0]))
                        }
                    }
                })
            }
        }
    }

    private fun initRecyclerView(
        recyclerView: ShimmerRecyclerView,
        data: List<DayListItem>
    ) {

        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toDayListItems())
        }
        val size =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        val myLayoutManager = StaggeredGridLayoutManager(size, GridLayoutManager.VERTICAL)

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = myLayoutManager
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? DayPlanItemRecyclerView)?.let {

            }
        }

        recyclerView.hideShimmerAdapter()
    }

    private fun List<DayListItem>.toDayListItems(): List<DayPlanItemRecyclerView> = this.map {

        DayPlanItemRecyclerView(it, this@TrackFoodsFragment)
    }

    override fun sendCheckedStatus(check: Boolean, meal: FoodSummery) {
        requestList.add(Pair(check, meal))
    }

    override fun sendWholeCheckedStatus(check: Boolean, day: DayListItem) {
        requestList.add(Pair(check, day.breakFast))
        requestList.add(Pair(check, day.lunch))
        requestList.add(Pair(check, day.dinner))
        requestList.add(Pair(check, day.snack))
    }

    override fun sendOneMealUnChecked() {

    }

    override fun regenerateOneMeal(meal: FoodSummery) {

    }

    override fun regenerateOneMealToHome(meal: FoodSummery) {
        if (isNetworkConnected(requireActivity())) {
            showConfirmReGenerateOneRecipeDialog(meal)
        } else {
            viewModel.showToast("Can not generate new Meal without internet!")
        }
    }

    override fun regenerateWholePlan(plan: DayListItem) {
        if (isNetworkConnected(requireActivity())) {
            showConfirmReGenerateDialog(plan)
        } else {
            viewModel.showToast("Can not generate new Meal Plan without internet!")
        }
    }

    private fun showConfirmReGenerateOneRecipeDialog(meal: FoodSummery) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        dialog.apply {
            setTitle("ReGenerate Plan")
            setMessage("Do you want to re-generate your meal plan for ${meal.mealLabel} at ${meal.localDate}?")
            setPositiveButton(
                "OK"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                viewModel.setDate(meal.localDate)

                launch {
                    viewModel.generatePlanRequest.await()
                        ?.observe(viewLifecycleOwner, Observer {
                            if (it != null)
                                if (it.isNotEmpty()) {
                                    val itemToAdd = when (meal.mealLabel) {
                                        "Breakfast" -> it[0].breakFast
                                        "Lunch" -> it[0].lunch
                                        "Dinner" -> it[0].dinner
                                        "Snack" -> it[0].snack
                                        else -> it[0].breakFast
                                    }
                                    saveNewlyGeneratedMealToRoom(
                                        meal.localDate,
                                        meal.mealLabel,
                                        itemToAdd
                                    )
                                }
                        })
                }
            }
            setNeutralButton(
                "Forget it"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            show()
        }
    }

    private fun showConfirmReGenerateDialog(plan: DayListItem) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        dialog.apply {
            setTitle("ReGenerate Plan")
            setMessage("Do you want to re-generate your meal plan for ${plan.breakFast.localDate}?")
            setPositiveButton(
                "OK"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                launch {
                    viewModel.generatePlanRequest.await()
                        ?.observe(viewLifecycleOwner, Observer {
                            if (it != null)
                                if (it.isNotEmpty())
                                    saveNewlyGeneratedDayPlanToRoom(
                                        plan.breakFast.localDate,
                                        it[0]
                                    )
                        })
                }
            }
            setNeutralButton(
                "Forget it"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            show()
        }
    }

    private fun saveNewlyGeneratedDayPlanToRoom(oldPlanDate: String, newPlan: DayListItem) =
        launch {
            newPlan.breakFast.localDate = oldPlanDate
            newPlan.lunch.localDate = oldPlanDate
            newPlan.dinner.localDate = oldPlanDate
            newPlan.snack.localDate = oldPlanDate

            newPlan.breakFast.mealLabel = "Breakfast"
            newPlan.lunch.mealLabel = "Lunch"
            newPlan.dinner.mealLabel = "Dinner"
            newPlan.snack.mealLabel = "Snack"
            viewModel.saveNewlyGeneratedDayPlanToRoom(oldPlanDate, newPlan)
            loadDataFromRoom()
        }


    private fun saveNewlyGeneratedMealToRoom(
        oldMealDate: String,
        oldMealLabel: String,
        newMeal: FoodSummery
    ) =
        launch {
            newMeal.localDate = oldMealDate
            newMeal.mealLabel = oldMealLabel
            val newDayPlan =
                DayListItem(
                    breakFast = newMeal,
                    lunch = newMeal,
                    dinner = newMeal,
                    snack = newMeal
                )
            val dayListSorted =
                DayListItem(
                    breakFast = newMeal,
                    lunch = newMeal,
                    dinner = newMeal,
                    snack = newMeal
                )

            viewModel.getSpecificDayMeal.await()
                ?.observe(viewLifecycleOwner, Observer { dayList ->
                    if (dayList != null) {
                        if (dayList.isNotEmpty()) {

                            for (item in dayList) {
                                when (item.mealLabel) {
                                    "Breakfast" -> dayListSorted.breakFast = item
                                    "Lunch" -> dayListSorted.lunch = item
                                    "Dinner" -> dayListSorted.dinner = item
                                    "Snack" -> dayListSorted.snack = item
                                }
                            }

                            if (numberOfTimes == 0) {
                                newDayPlan.apply {
                                    when (oldMealLabel) {
                                        "Breakfast" -> {
                                            lunch = dayListSorted.lunch
                                            dinner = dayListSorted.dinner
                                            snack = dayListSorted.snack
                                        }
                                        "Lunch" -> {
                                            breakFast = dayListSorted.breakFast
                                            dinner = dayListSorted.dinner
                                            snack = dayListSorted.snack
                                        }
                                        "Dinner" -> {
                                            breakFast = dayListSorted.breakFast
                                            lunch = dayListSorted.lunch
                                            snack = dayListSorted.snack
                                        }
                                        "Snack" -> {
                                            breakFast = dayListSorted.breakFast
                                            lunch = dayListSorted.lunch
                                            dinner = dayListSorted.dinner
                                        }
                                        else -> {

                                        }
                                    }
                                }

                                viewModel.saveNewlyGeneratedDayPlanToRoom(
                                    oldMealDate,
                                    newDayPlan
                                )

                                numberOfTimes = 1
                                loadDataFromRoom()
                            }
                        }
                    }
                })
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
