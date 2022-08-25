package com.example.heat.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.FragmentSearchBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.RecipeItemRecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

import androidx.core.widget.doOnTextChanged
import com.arlib.floatingsearchview.FloatingSearchView
import com.example.heat.data.datamodel.SearchQuery
import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.MealType

import com.example.heat.util.*
import android.app.Dialog

import android.view.Gravity
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heat.R
import com.example.heat.databinding.FilterBottomSheetDialogBinding
import com.example.heat.util.UiUtils.Companion.showToast
import com.example.heat.util.enumerian.RecipeViewType
import com.google.android.material.bottomsheet.BottomSheetDialog


class SearchFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: SearchViewModelFactory by instance()

    private lateinit var viewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    //TODO change the logics to mvvm style with sealed class in viewModel
    private fun bindUI() = launch {
        var searchQuery = SearchQuery(
            "",
            dietType = DietType.ANY_THING,
            mealType = MealType.all,
            0,
            10000
        )

        val recipesListAtFirst = viewModel.recipesListAtFirst.await()

        recipesListAtFirst.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            initRecyclerView(binding.searchedRecipesRecyclerView, list.results)
        })

        binding.apply {
            //TODO make change when submit query instead of while typing
            floatingSearchView.setOnQueryChangeListener { oldQuery, newQuery ->
                searchQuery.query = newQuery
                updaterSearchQuery(searchQuery)
            }
            filterSearch.setOnClickListener {
                searchQuery = showFilterBottomDialog(searchQuery)
            }

            //TODO make suggestion list work
        }
    }

    private fun getMealType(text: String?): MealType {
        return when (text) {
            MealType.all.toString() -> MealType.all
            MealType.breakfast.toString() -> MealType.breakfast
            MealType.main_course.toString() -> MealType.main_course
            MealType.dessert.toString() -> MealType.dessert
            MealType.appetizer.toString() -> MealType.appetizer
            MealType.salad.toString() -> MealType.salad
            MealType.bread.toString() -> MealType.bread
            MealType.soup.toString() -> MealType.soup
            MealType.beverage.toString() -> MealType.beverage
            MealType.sauce.toString() -> MealType.sauce
            MealType.fingerfood.toString() -> MealType.fingerfood
            MealType.drink.toString() -> MealType.drink
            else -> MealType.all
        }
    }

    private fun getDietType(text: String?): DietType {
        return when (text) {
            DietType.ANY_THING.toString() -> DietType.ANY_THING
            DietType.Vegetarian.toString() -> DietType.Vegetarian
            DietType.Vegan.toString() -> DietType.Vegan
            else -> DietType.ANY_THING
        }
    }

    private fun initRecyclerView(
        recyclerView: ShimmerRecyclerView,
        data: List<RecipeListItem>
    ) {

        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toRecipeListItems())
        }
        /*val size =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        val myLayoutManager = GridLayoutManager(context, size, GridLayoutManager.VERTICAL, false)*/

        val swipeHandler = object : SwipeToLikeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //val adapter = recipeRecycleView.adapter
                //todo send like to server
                showToast(requireContext(), "Swiped ${viewHolder.itemView.id}")
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? RecipeItemRecyclerView)?.let {
                val actionRecipeDetail =
                    SearchFragmentDirections.sendRecipeIdFromSearch(it.recipeItem.id)
                Navigation.findNavController(view).navigate(actionRecipeDetail)
            }
        }

        recyclerView.hideShimmerAdapter()
    }

    private fun List<RecipeListItem>.toRecipeListItems(): List<RecipeItemRecyclerView> = this.map {
        RecipeItemRecyclerView(it, RecipeViewType.LARGE)
    }

    private fun updaterSearchQuery(query: SearchQuery) {
        viewModel.setCurrentSearchQuery(query)
        bindSearchResult()
    }

    private fun bindSearchResult() {
        viewModel.recipesListFiltered.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer

            initRecyclerView(binding.searchedRecipesRecyclerView, list.results)
        })
    }

    private fun showFilterBottomDialog(searchQuery: SearchQuery):SearchQuery{

        val inflater = LayoutInflater.from(requireContext())
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)

        val binding = FilterBottomSheetDialogBinding.inflate(inflater)

        dialog.setContentView(binding.root)

        binding.apply {
            //setData from former searchQuery
            dietType.setText(searchQuery.dietType.toString())
            mealType.setText(searchQuery.mealType.toString())
            //TODO range slider did not set former values
            rangeSlider.setValues(searchQuery.minCalorie.toFloat(), searchQuery.maxCalorie.toFloat())

            val _dietType = resources.getStringArray(R.array.diet_type)
            val arrayAdapterDiet = ArrayAdapter(requireContext(), R.layout.dropdown_item, _dietType)
            dietType.setAdapter(arrayAdapterDiet)

            val _mealType = resources.getStringArray(R.array.meal_type)
            val arrayAdapterMeal = ArrayAdapter(requireContext(), R.layout.dropdown_item, _mealType)
            mealType.setAdapter(arrayAdapterMeal)

            //no pop up keyboard on focus
            dietType.showSoftInputOnFocus = false
            mealType.showSoftInputOnFocus = false

            mealType.doOnTextChanged { text, start, count, after ->
                searchQuery.mealType = getMealType(text.toString())
            }

            dietType.doOnTextChanged { text, start, count, after ->
                searchQuery.dietType = getDietType(text.toString())
            }
            searchQuery.minCalorie = rangeSlider.values[0].toInt()
            searchQuery.maxCalorie = rangeSlider.values[1].toInt()

            filterBtn.setOnClickListener {
                updaterSearchQuery(searchQuery)
                dialog.dismiss()
            }
        }
        dialog.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        dialog.show()

        return searchQuery
    }
}