package com.example.heat.ui.trackFood

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.databinding.FragmentTrackFoodsBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.DayPlanItemRecyclerView
import com.example.heat.util.SendEvent
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TrackFoodsFragment : ScopedFragment(), KodeinAware, SendEvent {
    override val kodein by closestKodein()
    private val viewModelFactory: TrackFoodsViewModelFactory by instance()

    private lateinit var viewModel: TrackFoodsViewModel

    private var _binding: FragmentTrackFoodsBinding? = null
    private val binding
        get() = _binding!!

    private val requestList = arrayListOf<Pair<Boolean, MealListItem>>()

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

        viewModel.loadFakeDateToRoom()

        viewModel.getFakeData.await().observe(viewLifecycleOwner, Observer {
            val data: ArrayList<DayListItem> = arrayListOf()

            var i = 0
            while (i + 3 < it.size) {
                val list: ArrayList<MealListItem> = arrayListOf()

                list.addAll(it.slice(i..i + 3))
                data.add(DayListItem(list))
                i += 4
            }

            initRecyclerView(binding.mealRecyclerView, data)
        })
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

    override fun sendCheckedStatus(check: Boolean, meal: MealListItem) {
        requestList.add(Pair(check, meal))
    }

    override fun sendWholeCheckedStatus(check: Boolean, day: DayListItem) {
        for (meal in day.dayPlan)
            requestList.add(Pair(check, meal))
            //viewModel.eatOrUnEatFoodToRoom(meal, check)
    }

    override fun sendOneMealUnChecked() {

    }

}
