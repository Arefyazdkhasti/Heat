package com.example.heat.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.recipes.ALL
import com.example.heat.util.enumerian.NavigateAction
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import androidx.constraintlayout.widget.ConstraintSet
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.NutritionType
import com.example.heat.data.datamodel.recipeDetail.Nutrition
import com.example.heat.databinding.FragmentHomeBinding
import com.example.heat.ui.itemRecyclerView.NutritionChartItemRecyclerView
import com.example.heat.util.UiUtils.Companion.getDayOrWeekFromSetting
import com.example.heat.util.UiUtils.Companion.showToast
import com.example.heat.util.UiUtils.Companion.stringFromResourcesByName
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.job

class HomeFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: HomeViewModelFactory by instance()

    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {

        binding.apply {
            viewModel.mealDbSize.await().observe(viewLifecycleOwner, Observer {
                if (it == 0) {
                    todayMealsRecipesListView.visibility = View.INVISIBLE
                    processTitle.visibility = View.INVISIBLE
                    nutritionChartsRecyclerView.visibility = View.GONE
                    layoutGenerateFood.visibility = View.GONE
                    todayMealsTitleWhenNoPlan.visibility = View.VISIBLE

                    layoutGenerateFood.setOnClickListener {
                        todayMealsRecipesListView.visibility = View.VISIBLE
                        processTitle.visibility = View.VISIBLE
                        nutritionChartsRecyclerView.visibility = View.VISIBLE
                        layoutGenerateFood.visibility = View.GONE
                        todayMealsTitleWhenNoPlan.visibility = View.GONE
                        initRecyclerViewTodayMeals(viewModel.generateDayFakeData())
                    }
                } else {
                    todayMealsRecipesListView.visibility = View.VISIBLE
                    processTitle.visibility = View.VISIBLE
                    nutritionChartsRecyclerView.visibility = View.VISIBLE
                    layoutGenerateFood.visibility = View.GONE
                    todayMealsTitleWhenNoPlan.visibility = View.GONE
                    initRecyclerViewTodayMeals(viewModel.generateDayFakeData())
                }
            })


            when {
                getDayOrWeekFromSetting(requireContext()) == "One Day" -> {
                    processTitle.text = "Day Prcocess"
                    viewModel.userDayMeal.await().observe(viewLifecycleOwner, Observer { list ->
                        var eatenCalories = 0
                        var allCalories = 0
                        for (item in list) {
                            if (item.eaten)
                                eatenCalories += item.calories
                            allCalories += item.calories
                        }
                        val result = if (allCalories != 0) {
                            val calories = NutritionType(
                                "Calories",
                                R.drawable.ic_calories,
                                ((eatenCalories.toFloat() / (allCalories.toFloat())) * 100).toInt()
                            )
                            listOf(calories)
                        } else listOf()
                        if (result.isEmpty()) {
                            layout.visibility = View.GONE
                            nutritionChartsRecyclerView.visibility = View.GONE
                        }
                        initRecyclerViewNutritionChart(nutritionChartsRecyclerView, result)
                    })
                }
                else -> {
                    processTitle.text ="Week Process"
                    viewModel.userWeekMeals.await().observe(viewLifecycleOwner, Observer { list ->
                        var eatenCalories = 0
                        var allCalories = 0
                        for (item in list) {
                            if (item.eaten)
                                eatenCalories += item.calories
                            allCalories += item.calories
                        }
                        val result = if (allCalories != 0) {
                            val calories = NutritionType(
                                "Calories",
                                R.drawable.ic_calories,
                                ((eatenCalories.toFloat() / (allCalories.toFloat())) * 100).toInt()
                            )
                            listOf(calories)
                        } else listOf()
                        if (result.isEmpty()) {
                            layout.visibility = View.GONE
                            nutritionChartsRecyclerView.visibility = View.GONE
                        }
                        initRecyclerViewNutritionChart(nutritionChartsRecyclerView, result)
                    })
                }
            }
        }
    }

    private fun initRecyclerViewTodayMeals(items: List<RecipeListItem>) {
        binding.todayMealsRecipesListView.initRecycler(items, ALL, NavigateAction.TRACK_FOODS)
    }

    private fun initRecyclerViewNutritionChart(
        recyclerView: ShimmerRecyclerView,
        data: List<NutritionType>
    ) {

        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toNutritionTypeItems())
        }


        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? NutritionChartItemRecyclerView)?.let {

            }
        }

        recyclerView.hideShimmerAdapter()
    }

    private fun List<NutritionType>.toNutritionTypeItems(): List<NutritionChartItemRecyclerView> =
        this.map {
            NutritionChartItemRecyclerView(it)
        }

}
