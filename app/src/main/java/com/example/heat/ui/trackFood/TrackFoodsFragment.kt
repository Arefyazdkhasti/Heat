package com.example.heat.ui.trackFood

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.databinding.FragmentTrackFoodsBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.DayPlanItemRecyclerView
import com.example.heat.util.SendEvent
import com.example.heat.util.UiUtils.Companion.getDayOrWeekFromSetting
import com.example.heat.util.UiUtils.Companion.stringFromResourcesByName
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
                        list[0].mealLabel = "Breakfast"
                        list[1].mealLabel = "Lunch"
                        list[2].mealLabel = "Dinner"
                        list[3].mealLabel = "Snack"
                        data.add(DayListItem(list[0],list[1],list[2],list[3]))
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
                                list[0].mealLabel = "Breakfast"
                                list[1].mealLabel = "Lunch"
                                list[2].mealLabel = "Dinner"
                                list[3].mealLabel = "Snack"
                                data.add(DayListItem(list[0],list[1],list[2],list[3]))
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

    override fun regenerateWholePlan(plan: DayListItem) {

    }
}
