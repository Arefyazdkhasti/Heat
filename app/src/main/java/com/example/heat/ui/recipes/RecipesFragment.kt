package com.example.heat.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.heat.databinding.FragmentRecipesBinding
import com.example.heat.ui.base.ScopedFragment
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


const val ALL = "all"
const val BREAKFAST = "breakfast"
const val MAIN_COURSE = "main course"
const val SNACK = "snack"

class RecipesFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: RecipesViewModelFactory by instance()

    private lateinit var viewModel: RecipesViewModel

    private var _binding: FragmentRecipesBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecipesViewModel::class.java)

        /*val api = RecipesApiService(ConnectivityInterceptorImpl(this.requireContext()))
        GlobalScope.launch {
            val test = api.getRecipesListAsync(BREAKFAST).await()
            println("PSHM ==> $test")
        }*/
    }
    private fun bindUI() = launch {

        /*val allRecipesList = viewModel.allRecipeList.await()

        allRecipesList.observe(viewLifecycleOwner, Observer { allList ->
            if(allList == null) return@Observer

            initRecyclerViewAll(allList.results)

        })

        val breakfastRecipesList = viewModel.breakfastRecipesList.await()

        breakfastRecipesList.observe(viewLifecycleOwner, Observer { breakfastList ->
            if(breakfastList == null) return@Observer

            initRecyclerViewBreakfast(breakfastList.results)
        })

        val snackRecipesList = viewModel.snackRecipesList.await()

        snackRecipesList.observe(viewLifecycleOwner, Observer { snackList ->
            if(snackList == null) return@Observer

            initRecyclerViewSnack(snackList.results)
        })

        val mainCourseRecipesList = viewModel.mainCourseRecipesList.await()

        mainCourseRecipesList.observe(viewLifecycleOwner, Observer { mainCourseList ->
            if(mainCourseList == null) return@Observer

            initRecyclerViewMainCourse(mainCourseList.results)

        })*/
    }

    /*private fun initRecyclerViewAll(items: List<RecipeListItem>) {
        binding?.allRecipesListView?.initRecycler(items, ALL , NavigateAction.SEE_ALL)
    }

    private fun initRecyclerViewBreakfast(items: List<RecipeListItem>) {
        binding?.breakfastRecipesListView?.initRecycler(items, BREAKFAST, NavigateAction.SEE_ALL)
    }

    private fun initRecyclerViewSnack(items: List<RecipeListItem>) {
        binding?.snackRecipesListView?.initRecycler(items, SNACK, NavigateAction.SEE_ALL)
    }


    private fun initRecyclerViewMainCourse(items: List<RecipeListItem>) {
        binding?.mainCourseRecipesListView?.initRecycler(items, MAIN_COURSE, NavigateAction.SEE_ALL)
    }*/


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}