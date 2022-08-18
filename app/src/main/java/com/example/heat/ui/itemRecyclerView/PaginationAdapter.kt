package com.example.heat.ui.itemRecyclerView

import android.content.Context
import android.graphics.Movie

import androidx.recyclerview.widget.RecyclerView


import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.heat.R
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.ItemProgressBinding
import com.example.heat.databinding.RecipeItemBinding
import com.example.heat.util.UiUtils
import java.util.*


class PaginationAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val context = context
    private var recipeList: MutableList<RecipeListItem>? = LinkedList()
    private var isLoadingAdded = false

    fun setRecipeList(movieList: MutableList<RecipeListItem>?) {
        this.recipeList = movieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.recipe_item, parent, false)
                viewHolder = RecipeViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading: View = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingViewHolder(viewLoading)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = recipeList?.get(position)
        if(movie != null) {
            when (getItemViewType(position)) {
                ITEM -> {
                    val recipeViewHolder = holder as RecipeViewHolder
                    recipeViewHolder.recipeTitle.text = movie.title
                    UiUtils.setImageWithGlideWithContext(
                        context,
                        movie.image,
                        recipeViewHolder.recipeImage
                    )
                }
                LOADING -> {
                    val loadingViewHolder = holder as LoadingViewHolder
                    loadingViewHolder.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (recipeList == null) 0 else recipeList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == recipeList!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        //add(Movie())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = recipeList!!.size - 1
        val result = getItem(position)
        if (result != null) {
            recipeList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun add(recipeListItem: RecipeListItem) {
        recipeList!!.add(recipeListItem)
        notifyItemInserted(recipeList!!.size - 1)
    }

    fun addAll(moveResults: List<RecipeListItem>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun getItem(position: Int): RecipeListItem? {
        return recipeList?.get(position)
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecipeItemBinding.bind(itemView)

         var recipeTitle: AppCompatTextView = binding.recipeName
         var recipeImage: AppCompatImageView = binding.recipeImage

    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemProgressBinding.bind(itemView)


         var progressBar = binding.progressBar

    }

    companion object {
        private const val LOADING = 0
        private const val ITEM = 1
    }


}