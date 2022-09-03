package com.example.heat.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.NutritionType
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.databinding.FragmentHomeBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.NutritionChartItemRecyclerView
import com.example.heat.ui.recipes.ALL
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UiUtils.Companion.getCurrentDate
import com.example.heat.util.UiUtils.Companion.getDayOrWeekFromSetting
import com.example.heat.util.UserIDManager
import com.example.heat.util.enumerian.NavigateAction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
@RequiresApi(Build.VERSION_CODES.O)
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
        //Delete old records from local database
        viewModel.deletePreviousRecords(getCurrentDate())

        val userID = getUserIDFromDataStore()
        viewModel.setUserID(userID)

        binding.apply {
            viewModel.mealDbSize.await().observe(viewLifecycleOwner, Observer {
                println("local db size -> $it")
                if (it == 0) {
                    todayMealsRecipesListView.visibility = View.INVISIBLE
                    processTitle.visibility = View.INVISIBLE
                    nutritionChartsRecyclerView.visibility = View.GONE
                    layoutGenerateFood.visibility = View.VISIBLE
                    todayMealsTitleWhenNoPlan.visibility = View.VISIBLE
                    layoutGenerateFood.setOnClickListener {
                        showConfirmDialog()
                    }
                } else {
                    todayMealsRecipesListView.visibility = View.VISIBLE
                    processTitle.visibility = View.VISIBLE
                    nutritionChartsRecyclerView.visibility = View.VISIBLE
                    layoutGenerateFood.visibility = View.INVISIBLE
                    todayMealsTitleWhenNoPlan.visibility = View.INVISIBLE
                    initRecyclerViewTodayMeals(viewModel.generateDayFakeData())
                    initCharts()
                }
            })

        }
    }

    private fun initCharts() = launch {
        binding.apply {

            when {
                getDayOrWeekFromSetting(requireContext()) == "One Day" -> {
                    processTitle.text = "Day Prcocess"
                    viewModel.userDayMeal.await().observe(viewLifecycleOwner, Observer { list ->
                        var eatenCalories = 0.0
                        var allCalories = 0.0
                        for (item in list) {
                            if (item.eaten)
                                eatenCalories += item.calorie.amount
                            allCalories += item.calorie.amount
                        }
                        var eatenProtein = 0.0
                        var allProtein = 0.0
                        for (item in list) {
                            if (item.eaten)
                                eatenProtein += item.protein.amount
                            allProtein += item.protein.amount
                        }
                        var eatenFat = 0.0
                        var allFat = 0.0
                        for (item in list) {
                            if (item.eaten)
                                eatenFat += item.fat.amount
                            allFat += item.fat.amount
                        }
                        var eatenCarbo = 0.0
                        var allCarbo = 0.0
                        for (item in list) {
                            if (item.eaten)
                                eatenCarbo += item.carbohydrates.amount
                            allCarbo += item.carbohydrates.amount
                        }
                        var result = arrayListOf<NutritionType>()

                        if (allCalories != 0.0) {
                            val calories = NutritionType(
                                "Calories",
                                R.drawable.ic_calories,
                                ((eatenCalories.toFloat() / (allCalories.toFloat())) * 100).toInt()
                            )
                            result.add(calories)
                        }
                        if (allProtein != 0.0) {
                            val protein = NutritionType(
                                "Protein",
                                R.drawable.ic_protein,
                                ((eatenProtein.toFloat() / (allProtein.toFloat())) * 100).toInt()
                            )
                            result.add(protein)
                        }
                        if (allFat != 0.0) {
                            val fat = NutritionType(
                                "Fat",
                                R.drawable.ic_fat,
                                ((eatenFat.toFloat() / (allFat.toFloat())) * 100).toInt()
                            )
                            result.add(fat)
                        }
                        if (allCarbo != 0.0) {
                            val carbo = NutritionType(
                                "Carbo",
                                R.drawable.ic_carbo,
                                ((eatenCarbo.toFloat() / (allCarbo.toFloat())) * 100).toInt()
                            )
                            result.add(carbo)
                        }

                        if (result.isEmpty()) {
                            layout.visibility = View.GONE
                            nutritionChartsRecyclerView.visibility = View.GONE
                        }
                        initRecyclerViewNutritionChart(nutritionChartsRecyclerView, result)
                    }
                    )
                }
                else -> {
                    processTitle.text = "Week Process"
                    viewModel.userWeekMeals.await().observe(viewLifecycleOwner, Observer { list ->
                        var eatenCalories = 0.0
                        var allCalories = 0.0
                        for (item in list) {
                            if (item.eaten)
                                eatenCalories += item.calorie.amount
                            allCalories += item.calorie.amount
                        }
                        var eatenProtein = 0.0
                        var allProtein = 0.0
                        for (item in list) {
                            if (item.eaten)
                                eatenProtein += item.protein.amount
                            allProtein += item.protein.amount
                        }
                        var eatenFat = 0.0
                        var allFat = 0.0
                        for (item in list) {
                            if (item.eaten)
                                eatenFat += item.fat.amount
                            allFat += item.fat.amount
                        }
                        var eatenCarbo = 0.0
                        var allCarbo = 0.0
                        for (item in list) {
                            if (item.eaten)
                                eatenCarbo += item.carbohydrates.amount
                            allCarbo += item.carbohydrates.amount
                        }
                        val result = arrayListOf<NutritionType>()

                        if (allCalories != 0.0) {
                            val calories = NutritionType(
                                "Calories",
                                R.drawable.ic_calories,
                                ((eatenCalories.toFloat() / (allCalories.toFloat())) * 100).toInt()
                            )
                            result.add(calories)
                        }
                        if (allProtein != 0.0) {
                            val protein = NutritionType(
                                "Protein",
                                R.drawable.ic_protein,
                                ((eatenProtein.toFloat() / (allProtein.toFloat())) * 100).toInt()
                            )
                            result.add(protein)
                        }
                        if (allFat != 0.0) {
                            val fat = NutritionType(
                                "Fat",
                                R.drawable.ic_fat,
                                ((eatenFat.toFloat() / (allFat.toFloat())) * 100).toInt()
                            )
                            result.add(fat)
                        }
                        if (allCarbo != 0.0) {
                            val carbo = NutritionType(
                                "Carbo",
                                R.drawable.ic_carbo,
                                ((eatenCarbo.toFloat() / (allCarbo.toFloat())) * 100).toInt()
                            )
                            result.add(carbo)
                        }

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

    private fun savePlanToRoom() = launch {
        viewModel.generatePlanRequest.await()?.observe(viewLifecycleOwner, Observer {
            viewModel.saveFoodToRoom(it)

            initRecyclerViewTodayMeals(viewModel.generateDayFakeData())
            binding.todayMealsRecipesListView.visibility = View.VISIBLE
            binding.processTitle.visibility = View.VISIBLE
            binding.nutritionChartsRecyclerView.visibility = View.VISIBLE
            binding.layoutGenerateFood.visibility = View.INVISIBLE
            binding.todayMealsTitleWhenNoPlan.visibility = View.INVISIBLE
        })
    }

    private fun showConfirmDialog() {
        var days = ""
        var number = 0
        if (getDayOrWeekFromSetting(requireContext()) == "One Day") {
            days = "1 day"
            number = 1
        } else {
            days = "7 days"
            number = 7
        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        dialog.apply {
            setTitle("Generate Plan")
            setMessage("Your plan will be generated for $days according to your preferences.")
            setPositiveButton(
                "OK"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                viewModel.setNumberOfDays(number)
                viewModel.setUserID(getUserIDFromDataStore())
                savePlanToRoom()
            }
            setNeutralButton(
                "Forget it"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            show()
        }

    }

    private fun initRecyclerViewTodayMeals(items: List<FoodSummery>) = launch {
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

    private fun getUserIDFromDataStore(): Int {
        val dataStore = context?.dataStore
        var id = 0
        if (dataStore != null) {
            val userManager = UserIDManager(dataStore)
            userManager.userIDFlow.asLiveData().observe(viewLifecycleOwner, {
                if (it != null) {
                    id = it
                }
            })
        }
        return id
    }
}
