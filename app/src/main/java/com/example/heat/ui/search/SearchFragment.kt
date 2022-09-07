package com.example.heat.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.SearchQuery
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.databinding.FilterBottomSheetDialogBinding
import com.example.heat.databinding.FragmentSearchBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.RecipeItemRecyclerView
import com.example.heat.util.*
import com.example.heat.util.enumerian.Cuisine
import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.MealType
import com.example.heat.util.enumerian.RecipeViewType
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory


class SearchFragment : ScopedFragment(), KodeinAware, ErrorHandling {

    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((ErrorHandling) -> SearchViewModelFactory) by factory()

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

        //if(checkForInternet(requireActivity(),lifecycle))
        bindUI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            viewModelFactoryInstanceFactory(this@SearchFragment)
        )[SearchViewModel::class.java]
    }

    //TODO change the logics to mvvm style with sealed class in viewModel
    private fun bindUI() = launch {
        var searchQuery = SearchQuery(
            Cuisine.NONE,
            dietType = DietType.NONE,
            "",
            1700,
            mealType = MealType.NONE,
            0
        )

        val recipesListAtFirst = viewModel.recipesListAtFirst.await()

        recipesListAtFirst.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            initRecyclerView(binding.searchedRecipesRecyclerView, list)
        })

        binding.apply {
            floatingSearchView.setOnQueryChangeListener { oldQuery, newQuery ->
                searchQuery.keyword = newQuery
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
            getString(R.string.filter_meal_type_all) -> MealType.NONE
            getString(R.string.filter_meal_type_breakfast) -> MealType.BREAKFAST
            getString(R.string.filter_meal_type_main_course) -> MealType.MAIN_COURSE
            getString(R.string.filter_meal_type_dessert) -> MealType.DESSERT
            getString(R.string.filter_meal_type_appetizer) -> MealType.APPETIZER
            getString(R.string.filter_meal_type_salad) -> MealType.SALAD
            getString(R.string.filter_meal_type_bread) -> MealType.BREAD
            getString(R.string.filter_meal_type_soup) -> MealType.SOUP
            getString(R.string.filter_meal_type_beverage) -> MealType.BEVERAGE
            getString(R.string.filter_meal_type_sauce) -> MealType.SAUCE
            getString(R.string.filter_meal_type_finger_food) -> MealType.FINGER_FOOD
            getString(R.string.filter_meal_type_drink) -> MealType.DRINK
            getString(R.string.filter_meal_type_snack) -> MealType.SNACK
            getString(R.string.filter_meal_type_side_dish) -> MealType.SIDE_DISH
            else -> MealType.NONE
        }
    }

    private fun getDietType(text: String?): DietType {
        return when (text) {
            getString(R.string.filter_diet_type_all) -> DietType.NONE
            getString(R.string.filter_diet_type_vegetarian) -> DietType.VEGETARIAN
            getString(R.string.filter_diet_type_vegan) -> DietType.VEGAN
            getString(R.string.filter_diet_type_diaryFree) -> DietType.DAIRY_FREE
            getString(R.string.filter_diet_type_glutenFree) -> DietType.GLUTEN_FREE
            else -> DietType.NONE
        }
    }

    private fun initRecyclerView(
        recyclerView: ShimmerRecyclerView,
        data: List<FoodSummery>
    ) {

        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toRecipeListItems())
        }

        val swipeHandler = object : SwipeToLikeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition;
                val recipeSwiped = data[position]

                viewModel.setUserID(
                    UiUtils.getUserIDFromDataStore(
                        requireContext(),
                        viewLifecycleOwner
                    ), recipeSwiped.id
                )
                sendLikeRequest()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? RecipeItemRecyclerView)?.let {
                val actionRecipeDetail =
                    SearchFragmentDirections.sendRecipeIdFromSearch(it.foodSummery.id)
                Navigation.findNavController(view).navigate(actionRecipeDetail)
            }
        }

        recyclerView.hideShimmerAdapter()
    }

    private fun sendLikeRequest() = launch {
        viewModel.likeFood.await()?.observe(viewLifecycleOwner, Observer {
            Log.i("LIKE_FROM_SEARCH", "food liked -> ${it.status}")
        })
    }

    private fun List<FoodSummery>.toRecipeListItems(): List<RecipeItemRecyclerView> = this.map {
        RecipeItemRecyclerView(it, RecipeViewType.LARGE)
    }

    private fun updaterSearchQuery(query: SearchQuery) {
        viewModel.setCurrentSearchQuery(query)
        bindSearchResult()
    }

    private fun bindSearchResult() {
        viewModel.recipesListFiltered.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) return@Observer
            if (list.isNotEmpty())
                initRecyclerView(binding.searchedRecipesRecyclerView, list)
            else
                binding.animationNoResult.visibility = View.VISIBLE
        })
    }


    private fun showFilterBottomDialog(searchQuery: SearchQuery): SearchQuery {

        val inflater = LayoutInflater.from(requireContext())
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)

        val binding = FilterBottomSheetDialogBinding.inflate(inflater)

        dialog.setContentView(binding.root)

        binding.apply {
            //setData from former searchQuery
            dietType.setText(searchQuery.dietType.toString())
            mealType.setText(searchQuery.mealType.toString())
            cuisineType.setText(searchQuery.cuisine.toString())
            //TODO range slider did not set former values
            rangeSlider.setValues(
                searchQuery.minCalorie.toFloat(),
                searchQuery.maxCalorie.toFloat()
            )

            val _dietType = resources.getStringArray(R.array.diet_type)
            val arrayAdapterDiet = ArrayAdapter(requireContext(), R.layout.dropdown_item, _dietType)
            dietType.setAdapter(arrayAdapterDiet)

            val _mealType = resources.getStringArray(R.array.meal_type)
            val arrayAdapterMeal = ArrayAdapter(requireContext(), R.layout.dropdown_item, _mealType)
            mealType.setAdapter(arrayAdapterMeal)

            val _cuisineType = resources.getStringArray(R.array.cuisine_type)
            val arrayAdapterCuisine =
                ArrayAdapter(requireContext(), R.layout.dropdown_item, _cuisineType)
            cuisineType.setAdapter(arrayAdapterCuisine)

            //no pop up keyboard on focus
            dietType.showSoftInputOnFocus = false
            mealType.showSoftInputOnFocus = false
            cuisineType.showSoftInputOnFocus = false

            mealType.doOnTextChanged { text, start, count, after ->
                searchQuery.mealType = getMealType(text.toString())
            }

            dietType.doOnTextChanged { text, start, count, after ->
                searchQuery.dietType = getDietType(text.toString())
            }

            cuisineType.doOnTextChanged { text, start, count, after ->
                searchQuery.cuisine = Cuisine.valueOf(text.toString())
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

    override fun socketTimeOutEvent() {
        handleErrors("Socket time out. Try again.")
    }

    override fun noConnectionEvent() {
        handleErrors("No Connection. Try again.")
    }

    override fun otherErrorEvent() {
        handleErrors("Some error occurred.  Try again.")
    }

    private fun handleErrors(msg: String) {
        requireActivity().runOnUiThread {
            binding.animationNoResult.visibility = View.VISIBLE
            binding.searchedRecipesRecyclerView.visibility = View.INVISIBLE
        }

        Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction("Try again") {
                    binding.animationNoResult.visibility = View.GONE
                    binding.searchedRecipesRecyclerView.visibility = View.VISIBLE
                    bindUI()
                }
                anchorView = binding.filterSearch
                show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}