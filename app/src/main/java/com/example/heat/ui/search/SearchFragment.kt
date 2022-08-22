package com.example.heat.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
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
import com.example.heat.data.datamodel.SearchQuery
import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.MealType

import com.example.heat.util.*


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
        val searchQuery = SearchQuery("", dietType = DietType.ANY_THING, mealType = MealType.all)

        val _dietType = resources.getStringArray(R.array.diet_type)
        val arrayAdapterDiet = ArrayAdapter(requireContext(), R.layout.dropdown_item, _dietType)
        binding.dietType.setAdapter(arrayAdapterDiet)

        val _mealType = resources.getStringArray(R.array.meal_type)
        val arrayAdapterMeal = ArrayAdapter(requireContext(), R.layout.dropdown_item, _mealType)
        binding.mealType.setAdapter(arrayAdapterMeal)

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
            mealType.doOnTextChanged { text, start, count, after ->
                searchQuery.mealType = getMealType(text.toString())
                updaterSearchQuery(searchQuery)
            }

            dietType.doOnTextChanged { text, start, count, after ->
                searchQuery.dietType = getDietType(text.toString())
                updaterSearchQuery(searchQuery)
            }
            //TODO make suggestion list work
            floatingSearchView.setOnBindSuggestionCallback { suggestionView, leftIcon, textView, item, itemPosition ->
                //here you can set some attributes for the suggestion's left icon and text. For example,
                //you can choose your favorite image-loading library for setting the left icon's image.
            }
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
        val size =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        val myLayoutManager = GridLayoutManager(context, size, GridLayoutManager.VERTICAL, false)

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = myLayoutManager
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
        RecipeItemRecyclerView(it)
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
}