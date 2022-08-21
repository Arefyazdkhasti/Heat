package com.example.heat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.FragmentHomeBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.recipes.ALL
import com.example.heat.util.enum.NavigateAction
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


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

    @DelicateCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
        initRecyclerViewTodayMeals(viewModel.generateFakeData())

        binding.apply{
            fat.initPieChart(arrayListOf<PieEntry>(PieEntry(13f),PieEntry(26f)))
        }
    }

    private fun initRecyclerViewTodayMeals(items: List<RecipeListItem>) {
        binding.todayMealsRecipesListView.initRecycler(items, ALL, NavigateAction.TRACK_FOODS)
    }

}
