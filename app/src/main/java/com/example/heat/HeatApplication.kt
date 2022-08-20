package com.example.heat

import android.app.Application
import com.example.heat.data.data.ConnectivityInterceptor
import com.example.heat.data.data.ConnectivityInterceptorImpl
import com.example.heat.data.data.RecipesApiService
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.data.repository.RecipesRepositoryImpl
import com.example.heat.data.data.response.NetworkDataSource
import com.example.heat.data.data.response.NetworkDataSourceImpl
import com.example.heat.ui.profile.ProfileViewModelFactory
import com.example.heat.ui.recipeDetail.RecipeDetailViewModelFactory
import com.example.heat.ui.recipes.RecipesViewModelFactory
import com.example.heat.ui.search.SearchViewModelFactory
import com.example.heat.ui.seeAllRecipes.SeeAllRecipesViewModelFactory
import com.example.heat.ui.survey.SurveyViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class HeatApplication: Application(),KodeinAware{
    override val kodein = Kodein.lazy {
        import(androidXModule(this@HeatApplication))

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { RecipesApiService(instance()) }
        bind<NetworkDataSource>() with singleton { NetworkDataSourceImpl(instance()) }
        bind<RecipesRepository>() with singleton { RecipesRepositoryImpl(instance())}
        bind() from provider { RecipesViewModelFactory(instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
        bind() from provider { SearchViewModelFactory(instance()) }
        bind() from provider { SurveyViewModelFactory(instance()) }
        bind() from factory { recipeID:Int -> RecipeDetailViewModelFactory(recipeID,instance()) }
        bind() from factory { type:String -> SeeAllRecipesViewModelFactory(type,instance()) }


    }
}