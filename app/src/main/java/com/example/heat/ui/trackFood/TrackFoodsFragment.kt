package com.example.heat.ui.trackFood

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.EatenMealItem
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.FragmentHomeBinding
import com.example.heat.databinding.FragmentTrackFoodsBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.home.HomeViewModel
import com.example.heat.ui.home.HomeViewModelFactory
import com.example.heat.ui.itemRecyclerView.MealItemRecyclerView
import com.example.heat.util.SendEvent
import com.example.heat.util.UiUtils.Companion.showToast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.DelicateCoroutinesApi
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
            requireActivity().onBackPressed()
        }

        //viewModel.loadFakeDateToRoom()

        viewModel.getFakeData.await().observe(viewLifecycleOwner, Observer {
            initRecyclerView(binding.mealRecyclerView, it)
        })

        /*viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is TrackFoodsViewModel.TrackFoodTransactionEvents.CheckFood -> {

                    }
                    is TrackFoodsViewModel.TrackFoodTransactionEvents.UnCheckFood -> {

                    }
                }
            }
        }*/

    }

    private fun initRecyclerView(
        recyclerView: ShimmerRecyclerView,
        data: List<MealListItem>
    ) {

        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toRecipeListItems())
        }
        val size =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        val myLayoutManager = StaggeredGridLayoutManager(size, GridLayoutManager.VERTICAL)

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = myLayoutManager
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? MealItemRecyclerView)?.let {

            }
        }

        recyclerView.hideShimmerAdapter()
    }

    private fun List<MealListItem>.toRecipeListItems(): List<MealItemRecyclerView> = this.map {
        MealItemRecyclerView(it, this@TrackFoodsFragment)
    }

    override fun sendCheckedStatus(check: Boolean, meal: MealListItem) {
        showToast(requireContext(), "$check ${meal.id}")
        viewModel.eatOrUnEatFoodToRoom(meal, check)

    }
}
