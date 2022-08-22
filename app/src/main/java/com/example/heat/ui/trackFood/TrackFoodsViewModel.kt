package com.example.heat.ui.trackFood

import androidx.lifecycle.ViewModel
import com.example.heat.R
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.util.UiUtils.Companion.getStringFromResource

class TrackFoodsViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {


    fun generateFakeData(): List<MealListItem> {
        val breakfast = MealListItem(
            1,
            "https://simply-delicious-food.com/wp-content/uploads/2018/10/breakfast-board-500x500.jpg",
            "PNG",
            getStringFromResource(R.string.breakfast),
            "Italian",
            45,
            1,
            "Breakfast"
        )
        val lunch = MealListItem(
            2,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRoJBTc9RI3gHAWFG3-dRApc8TbPbvCPy4oS4gf6hcDTrEbK7L_pNUkyXXpDxt_9GZcFFQ&usqp=CAU",
            "PNG",
            getStringFromResource(R.string.lunch),
            "Iranian",
            36,
            2,
            "Lunch"
        )
        val dinner = MealListItem(
            3,
            "https://insanelygoodrecipes.com/wp-content/uploads/2021/03/Spaghetti-and-Meatballs-with-Tomato-Sauce-683x1024.png",
            "PNG",
            getStringFromResource(R.string.dinner),
            "Middle Eastern",
            65,
            1,
            "Dinner"
        )
        val snack = MealListItem(
            4,
            "https://img.freepik.com/free-vector/collection-delicious-snacks_23-2147914461.jpg",
            "PNG",
            getStringFromResource(R.string.snack),
            "Cuban",
            26,
            3,
            "Snack"
        )
        //val recipeList = RecipeList(1, 0, listOf(breakfast), 1)

        return listOf(breakfast,lunch,dinner,snack)
    }
}