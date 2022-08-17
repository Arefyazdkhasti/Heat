package com.example.heat.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.FragmentSearchBinding
import com.example.heat.ui.base.ScopedFragment
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


const val BREAKFAST = "breakfast"
const val MAIN_COURSE = "main course"
const val SNACK = "snack"

class SearchFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: SearchViewModelFactory by instance()

    private lateinit var viewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    @DelicateCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        /*val api = RecipesApiService(ConnectivityInterceptorImpl(this.requireContext()))
        GlobalScope.launch {
            val test = api.getRecipesListAsync(BREAKFAST).await()
            println("PSHM ==> $test")
        }*/
    }
    private fun bindUI() = launch {
        val breakfastRecipesList = viewModel.breakfastRecipesList.await()

        breakfastRecipesList.observe(viewLifecycleOwner, Observer { breakfastList ->
            if(breakfastList == null) return@Observer

            binding?.breakfastRecipesListView?.hideShimmer()
            initRecyclerViewBreakfast(breakfastList.results)
        })

        val snackRecipesList = viewModel.snackRecipesList.await()

        snackRecipesList.observe(viewLifecycleOwner, Observer { snackList ->
            if(snackList == null) return@Observer

            binding?.snackRecipesListView?.hideShimmer()
            initRecyclerViewSnack(snackList.results)
        })

        val mainCourseRecipesList = viewModel.mainCourseRecipesList.await()

        mainCourseRecipesList.observe(viewLifecycleOwner, Observer { mainCourseList ->
            if(mainCourseList == null) return@Observer

            binding?.breakfastRecipesListView?.hideShimmer()
            initRecyclerViewMainCourse(mainCourseList.results)
        })
    }

    private fun initRecyclerViewBreakfast(items: List<RecipeListItem>) {
        binding?.breakfastRecipesListView?.initRecycler(items, BREAKFAST)
    }

    private fun initRecyclerViewSnack(items: List<RecipeListItem>) {
        binding?.snackRecipesListView?.initRecycler(items, SNACK)
    }


    private fun initRecyclerViewMainCourse(items: List<RecipeListItem>) {
        binding?.mainCourseRecipesListView?.initRecycler(items, MAIN_COURSE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}