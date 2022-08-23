package com.example.heat.ui.recipeDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.data.datamodel.recipeDetail.ExtendedIngredient
import com.example.heat.data.datamodel.recipeDetail.NutrientX
import com.example.heat.data.datamodel.recipeDetail.Step
import com.example.heat.databinding.FragmentRecipeDetailBinding
import com.example.heat.ui.MainActivity
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.IngredientItemRecyclerView
import com.example.heat.ui.itemRecyclerView.InstructionItemRecyclerView
import com.example.heat.ui.itemRecyclerView.NutritionItemRecyclerView
import com.example.heat.util.UiUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class RecipeDetailFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((Int) -> RecipeDetailViewModelFactory) by factory()

    private lateinit var viewModel: RecipeDetailViewModel

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { RecipeDetailFragmentArgs.fromBundle(it) }
        val recipeID = safeArgs?.recipeID
        if (recipeID != null)
            viewModel = ViewModelProvider(this, viewModelFactoryInstanceFactory(recipeID)).get(
                RecipeDetailViewModel::class.java
            )
        bindUI()
    }

    @SuppressLint("SetTextI18n")
    private fun bindUI() = launch {
        val recipeDetail = viewModel.recipeDetail.await()
        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
        recipeDetail.observe(viewLifecycleOwner, Observer { recipe ->
            binding.apply {


                UiUtils.setImageWithGlideWithContext(
                    requireContext(), recipe.image, recipeImage
                )
                recipeTitle.text = recipe.title

                val info =
                    "${recipe.readyInMinutes} min ${recipe.nutrition.nutrients[0].amount} ${recipe.nutrition.nutrients[0].unit} per Serving"
                recipeMinAndCalInformation.text = info

                initIngredientsRecyclerView(
                    recipeIngredientRecyclerView,
                    recipe.extendedIngredients
                )

                initNutritionRecyclerView(recipeNutritionRecyclerView, recipe.nutrition.nutrients)

                if (recipe.analyzedInstructions.isNotEmpty())
                    iniInstructionRecyclerView(
                        recipeInstructionRecyclerView,
                        recipe.analyzedInstructions[0].steps
                    )
                else {
                    recipeInstructionRecyclerView.visibility = View.GONE
                    instructionTitle.visibility = View.INVISIBLE
                }
            }
        })
    }

    //ingredients recyclerView
    private fun initIngredientsRecyclerView(
        recyclerView: ShimmerRecyclerView,
        data: List<ExtendedIngredient>
    ) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toIngredientsListItems())
        }

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        recyclerView.hideShimmerAdapter()
    }

    private fun List<ExtendedIngredient>.toIngredientsListItems(): List<IngredientItemRecyclerView> =
        this.map {
            IngredientItemRecyclerView(it)
        }

    //Nutrition recyclerView
    private fun initNutritionRecyclerView(
        recyclerView: ShimmerRecyclerView, data: List<NutrientX>
    ) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toNutritionListItems())
        }

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.HORIZONTAL)
        }
        recyclerView.hideShimmerAdapter()
    }

    private fun List<NutrientX>.toNutritionListItems(): List<NutritionItemRecyclerView> = this.map {
        NutritionItemRecyclerView(it)
    }

    //Instruction recyclerView
    private fun iniInstructionRecyclerView(recyclerView: ShimmerRecyclerView, data: List<Step>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toInstructionListItems())
        }

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerView.hideShimmerAdapter()
    }

    private fun List<Step>.toInstructionListItems(): List<InstructionItemRecyclerView> = this.map {
        InstructionItemRecyclerView(it)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}