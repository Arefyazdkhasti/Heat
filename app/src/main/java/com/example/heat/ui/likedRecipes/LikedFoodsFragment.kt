package com.example.heat.ui.likedRecipes

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.heat.R
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.databinding.FragmentLikedFoodsBinding
import com.example.heat.databinding.FragmentSearchBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.RecipeItemRecyclerView
import com.example.heat.ui.search.SearchFragmentDirections
import com.example.heat.ui.search.SearchViewModel
import com.example.heat.ui.search.SearchViewModelFactory
import com.example.heat.util.ErrorHandling
import com.example.heat.util.SwipeToLikeCallback
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.checkForInternet
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UserIDManager
import com.example.heat.util.enumerian.RecipeViewType
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

class LikedFoodsFragment : ScopedFragment(), KodeinAware, ErrorHandling {

    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((ErrorHandling) -> LikedFoodsViewModelFactory) by factory()

    private lateinit var viewModel: LikedFoodsViewModel

    private var _binding: FragmentLikedFoodsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedFoodsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            viewModelFactoryInstanceFactory(this@LikedFoodsFragment)
        )[LikedFoodsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //if (checkForInternet(requireActivity(), lifecycle))
            bindUI()
    }

    private fun bindUI() = launch {
        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModel.setUserID(UiUtils.getUserIDFromDataStore(requireContext(), viewLifecycleOwner))
        val handler = Handler()
        handler.postDelayed(Runnable {
            loadData()
        }, 100)
    }

    private fun loadData() = launch {
        viewModel.likedRecipesList.await()?.observe(viewLifecycleOwner, Observer {
            if (it != null)
                if (it.isNotEmpty())
                    initLikedRecipesRecyclerView(it)
                else
                    binding.animationEmptyList.visibility = View.VISIBLE

            binding.likedRecipesRecyclerView.hideShimmerAdapter()
        })
    }

    private fun initLikedRecipesRecyclerView(data: List<FoodSummery>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(data.toRecipeListItems())
        }


        binding.likedRecipesRecyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? RecipeItemRecyclerView)?.let {
                val actionRecipeDetail =
                    LikedFoodsFragmentDirections.actionLikedFoodsFragmentToRecipeDetailFragment(it.foodSummery.id)
                Navigation.findNavController(view).navigate(actionRecipeDetail)
            }
        }

    }


    private fun List<FoodSummery>.toRecipeListItems(): List<RecipeItemRecyclerView> = this.map {
        RecipeItemRecyclerView(it, RecipeViewType.LARGE)
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
            binding.animationEmptyList.visibility = View.VISIBLE
            binding.likedRecipesRecyclerView.visibility = View.INVISIBLE
        }

        Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction("Try again") {
                    binding.animationEmptyList.visibility = View.GONE
                    binding.likedRecipesRecyclerView.visibility = View.VISIBLE
                    bindUI()
                }
                anchorView = view.findViewById<RecyclerView>(R.id.bottom_nav)
                show()
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}