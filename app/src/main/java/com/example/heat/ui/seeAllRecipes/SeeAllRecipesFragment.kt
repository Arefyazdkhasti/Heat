package com.example.heat.ui.seeAllRecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.heat.databinding.FragmentSeeAllRecipesBinding
import com.example.heat.ui.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory


class SeeAllRecipesFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((String) -> SeeAllRecipesViewModelFactory) by factory()

    private lateinit var viewModel: SeeAllRecipesViewModel

    private var _binding: FragmentSeeAllRecipesBinding? = null
    private val binding
        get() = _binding!!

    private var pageNumber = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSeeAllRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { SeeAllRecipesFragmentArgs.fromBundle(it) }
        val type = safeArgs?.type
        if (type != null) {
            viewModel = ViewModelProvider(
                this,
                viewModelFactoryInstanceFactory(type)
            )[SeeAllRecipesViewModel::class.java]
        }
        bindUI(type)
        //initialButtons()
    }

    private fun bindUI(type:String?) = launch {
        /*val recipesList = viewModel.recipe.await()

        binding.apply {

            backArrow.setOnClickListener {
                requireActivity().onBackPressed()
            }
            if (type != null) binding.seeAllTitle.text = type

            recipesList.observe(viewLifecycleOwner, Observer { list ->
                if (list == null) return@Observer
                initRecyclerView(recipesRecyclerView, list.results)
            })
        }*/
    }

    /*private fun initialButtons() = launch {
        binding.nextPage.setOnClickListener {
            pageNumber++
            viewModel.setCurrentPage(pageNumber * 10)
            bindNewPage()
        }

        binding.previousPage.setOnClickListener {
            pageNumber--
            viewModel.setCurrentPage(pageNumber * 10)
            bindNewPage()
        }
    }

    private fun bindNewPage() {
        viewModel.recipePage.observe(viewLifecycleOwner) {
            initRecyclerView(binding.recipesRecyclerView, it.results)
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
                    SeeAllRecipesFragmentDirections.sendRecipeIdFromAll(it.foodSummery.id)
                Navigation.findNavController(view).navigate(actionRecipeDetail)
            }
        }

        recyclerView.hideShimmerAdapter()
    }

    private fun List<RecipeListItem>.toRecipeListItems(): List<RecipeItemRecyclerView> = this.map {
        RecipeItemRecyclerView(it,RecipeViewType.SMALL)
    }*/
}