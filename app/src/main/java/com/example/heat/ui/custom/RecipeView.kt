package com.example.heat.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.ui.itemRecyclerView.RecipeItemRecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class  RecipeView(
    contextInstant: Context,
    attrs: AttributeSet
) : FrameLayout(contextInstant, attrs) {

    private val listTitle: AppCompatTextView
    private val seeAll: AppCompatTextView
    private val movieRecycleView: ShimmerRecyclerView

    init {
        val view = inflate(context, R.layout.recipe_view, this)

        //get attrs
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecipeView)
        val title = typedArray.getString(R.styleable.RecipeView_title)
        //recycle type array after use
        typedArray.recycle()


        listTitle = view.findViewById(R.id.recipe_type)
        seeAll = view.findViewById(R.id.recipe_type_see_all)
        movieRecycleView = view.findViewById(R.id.recipe_recyclerView)

        listTitle.text = title

        movieRecycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)


    }

    private fun List<RecipeListItem>.toMovieItems(): List<RecipeItemRecyclerView> = this.map {
        RecipeItemRecyclerView(it)
    }

    fun initRecycler(data: List<RecipeListItem>, type: String) {

        movieRecycleView.showShimmerAdapter()

        seeAll.setOnClickListener{
            navigateToSeeAllFragment(type,it)
        }

        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toMovieItems())
        }

        movieRecycleView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? RecipeItemRecyclerView)?.let {
                //val actionDetailHome = HomeFragmentDirections.sendMovieId(it.movieItem.id)
                //Navigation.findNavController(view).navigate(actionDetailHome)
            }
        }

    }

    fun hideShimmer() {
        movieRecycleView.hideShimmerAdapter()
    }

    fun showShimmer() {
        movieRecycleView.showShimmerAdapter()
    }

    private fun navigateToSeeAllFragment(type: String,view: View) {
        //val navigate = HomeFragmentDirections.seeAll(type)
        //Navigation.findNavController(view).navigate(navigate)
    }



}