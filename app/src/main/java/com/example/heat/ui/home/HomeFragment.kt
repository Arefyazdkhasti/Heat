package com.example.heat.ui.home

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

import android.R
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.data.datamodel.NutritionType
import com.example.heat.data.datamodel.recipeDetail.Nutrition
import com.example.heat.databinding.FragmentHomeBinding
import com.example.heat.ui.itemRecyclerView.NutritionChartItemRecyclerView
import com.example.heat.util.UiUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

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
                    layoutGenerateFood.visibility = View.VISIBLE
                    todayMealsTitleWhenNoPlan.visibility = View.VISIBLE

                    layoutGenerateFood.setOnClickListener {
                        layoutGenerateFood.visibility = View.INVISIBLE
                        todayMealsTitleWhenNoPlan.visibility = View.INVISIBLE
                        initRecyclerViewTodayMeals(viewModel.generateDayFakeData())
                        todayMealsRecipesListView.visibility = View.VISIBLE
                        processTitle.visibility = View.VISIBLE
                        nutritionChartsRecyclerView.visibility = View.VISIBLE
                    }
                } else {
                    initRecyclerViewTodayMeals(viewModel.generateDayFakeData())
                    todayMealsRecipesListView.visibility = View.VISIBLE
                    layoutGenerateFood.visibility = View.INVISIBLE
                    todayMealsTitleWhenNoPlan.visibility = View.INVISIBLE
                }
            })

            /*val constraintLayout: ConstraintLayout = layout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                layout.id,
                ConstraintSet.START,
                layoutGenerateFood.id,
                ConstraintSet.START,
                0
            )
            constraintSet.connect(
                layout.id,
                ConstraintSet.END,
                layoutGenerateFood.id,
                ConstraintSet.END,
                0
            )
            constraintSet.connect(
                layout.id,
                ConstraintSet.TOP,
                layoutGenerateFood.id,
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.connect(
                layout.id,
                ConstraintSet.BOTTOM,
                nutritionGrid.id,
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.applyTo(constraintLayout)*/
            //load fake data to charts
            val nutritions = listOf(
                NutritionType("Calories", R.drawable.ic_btn_speak_now, 24),
                NutritionType("Carbohydrate", R.drawable.ic_btn_speak_now, 75),
                NutritionType("Fat", R.drawable.ic_btn_speak_now, 48),
                NutritionType("Protein", R.drawable.ic_btn_speak_now, 12)
            )

            initRecyclerViewNutritionChart(nutritionChartsRecyclerView, nutritions)
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
