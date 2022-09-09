package com.example.heat.ui.custom

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.ui.home.HomeFragmentDirections
import com.example.heat.ui.itemRecyclerView.RecipeItemRecyclerView
import com.example.heat.ui.recipes.RecipesFragmentDirections
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.showBubbleHelp
import com.example.heat.util.enumerian.NavigateAction
import com.example.heat.util.enumerian.RecipeViewType
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class RecipeView(
    contextInstant: Context,
    attrs: AttributeSet
) : FrameLayout(contextInstant, attrs) {

    private val listTitle: AppCompatTextView
    private val seeAll: AppCompatTextView
    private val recipeRecycleView: ShimmerRecyclerView

    init {
        val view = inflate(context, R.layout.recipe_view, this)

        //get attrs
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecipeView)
        val title = typedArray.getString(R.styleable.RecipeView_title)
        val action = typedArray.getString(R.styleable.RecipeView_action)
        //recycle type array after use
        typedArray.recycle()


        listTitle = view.findViewById(R.id.recipe_type)
        seeAll = view.findViewById(R.id.recipe_type_see_all)
        recipeRecycleView = view.findViewById(R.id.recipe_recyclerView)

        listTitle.text = title
        seeAll.text = action

        recipeRecycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)

        /*showBubbleHelp(
            context,
            "track your foods",
            "You can track your \ngenerated foods \nby clicking here.",
            seeAll
        )*/
    }

    private fun List<FoodSummery>.toRecipeListItems(): List<RecipeItemRecyclerView> = this.map {
        RecipeItemRecyclerView(it, RecipeViewType.SMALL)
    }

    fun initRecycler(data: List<FoodSummery>, type: String, navigateAction: NavigateAction) {

        recipeRecycleView.showShimmerAdapter()

        seeAll.setOnClickListener {
            when (navigateAction) {
                NavigateAction.SEE_ALL -> navigateToSeeAllFragment(type, it)
                NavigateAction.TRACK_FOODS -> navigateToTrackFoodsFragment(it)
            }
        }

        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toRecipeListItems())
        }


        recipeRecycleView.apply {
            adapter = groupAdapter
            layoutManager = GridLayoutManager(context, if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2, GridLayoutManager.VERTICAL, false)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? RecipeItemRecyclerView)?.let {
                when (navigateAction) {
                    NavigateAction.SEE_ALL -> {
                        val actionRecipeDetail =
                            RecipesFragmentDirections.sendRecipeId(it.foodSummery.id)
                        Navigation.findNavController(view).navigate(actionRecipeDetail)
                    }
                    NavigateAction.TRACK_FOODS -> {
                        /*val actionRecipeDetail =
                            HomeFragmentDirections.sendRecipeIDFromHome(it.foodSummery.id)
                        Navigation.findNavController(view).navigate(actionRecipeDetail)*/
                    }
                }
            }
        }
    }

    fun hideShimmer() {
        recipeRecycleView.hideShimmerAdapter()
    }

    fun showShimmer() {
        recipeRecycleView.showShimmerAdapter()
    }

    private fun navigateToSeeAllFragment(type: String, view: View) {
        val navigate = RecipesFragmentDirections.actionSearchFragmentToSeeAllRecipesFragment(type)
        Navigation.findNavController(view).navigate(navigate)
    }

    private fun navigateToTrackFoodsFragment( view: View) {
        val navigate = HomeFragmentDirections.navigateToTrackFoods()
        Navigation.findNavController(view).navigate(navigate)
    }

}