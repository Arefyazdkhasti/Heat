package com.example.heat.di

import android.app.Application
import com.example.heat.data.network.connection.ConnectivityInterceptor
import com.example.heat.data.network.connection.ConnectivityInterceptorImpl
import com.example.heat.data.network.HeatApiService
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.network.repository.HeatRepositoryImpl
import com.example.heat.data.network.response.NetworkDataSource
import com.example.heat.data.network.response.NetworkDataSourceImpl
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.data.local.repository.RoomRepositoryImpl
import com.example.heat.data.local.room.MealDataBase
import com.example.heat.data.local.room.UserPreferenceDataBase
import com.example.heat.ui.home.HomeViewModelFactory
import com.example.heat.ui.likedRecipes.LikedFoodsViewModelFactory
import com.example.heat.ui.login.LoginViewModelFactory
import com.example.heat.ui.profile.ProfileViewModelFactory
import com.example.heat.ui.recipeDetail.RecipeDetailViewModelFactory
import com.example.heat.ui.recipes.RecipesViewModelFactory
import com.example.heat.ui.register.RegisterViewModelFactory
import com.example.heat.ui.search.SearchViewModelFactory
import com.example.heat.ui.seeAllRecipes.SeeAllRecipesViewModelFactory
import com.example.heat.ui.setting.abstractGoal.AbstractGoalViewModelFactory
import com.example.heat.ui.setting.activeLevel.ActiveLevelViewModelFactory
import com.example.heat.ui.setting.dailyNutrition.DailyNutritionViewModelFactory
import com.example.heat.ui.setting.dietType.DietTypeViewModelFactory
import com.example.heat.ui.setting.disease.DiseaseViewModelFactory
import com.example.heat.ui.setting.ingredientAllergy.IngredientAllergyViewModelFactory
import com.example.heat.ui.setting.personalData.PersonalDataViewModelFactory
import com.example.heat.ui.splash.SplashScreenViewModelFactory
import com.example.heat.ui.survey.SurveyViewModelFactory
import com.example.heat.ui.trackFood.TrackFoodsViewModelFactory
import com.example.heat.util.ErrorHandling
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class HeatApplication: Application(),KodeinAware{
    override val kodein = Kodein.lazy {
        import(androidXModule(this@HeatApplication))

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { HeatApiService(instance()) }
        bind<NetworkDataSource>() with singleton { NetworkDataSourceImpl(instance()) }
        bind<UserPreferenceDataBase>() with singleton { UserPreferenceDataBase.getDatabase(instance()) }
        bind<MealDataBase>() with singleton { MealDataBase.getDatabase(instance()) }
        bind<HeatRepository>() with singleton { HeatRepositoryImpl(instance())}
        bind<RoomRepository>() with singleton { RoomRepositoryImpl(instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance(), instance()) }
        bind() from provider { RecipesViewModelFactory(instance()) }
        bind() from provider { LoginViewModelFactory(instance(),instance()) }
        bind() from provider { RegisterViewModelFactory(instance()) }
        bind() from provider { SplashScreenViewModelFactory() }
        bind() from provider { ProfileViewModelFactory(instance(),instance()) }
        bind() from factory { errorHandling: ErrorHandling -> SearchViewModelFactory(errorHandling,instance()) }
        bind() from factory { errorHandling: ErrorHandling -> LikedFoodsViewModelFactory(errorHandling,instance()) }
        bind() from provider { SurveyViewModelFactory(instance()) }
        bind() from provider { TrackFoodsViewModelFactory(instance(),instance()) }
        bind() from factory { recipeID:Int -> RecipeDetailViewModelFactory(recipeID,instance()) }
        bind() from factory { type:String -> SeeAllRecipesViewModelFactory(type,instance()) }
        bind() from factory { userPreference: UserPreferences? -> PersonalDataViewModelFactory(userPreference, instance(), instance()) }
        bind() from factory { userPreference: UserPreferences? -> ActiveLevelViewModelFactory(userPreference, instance()) }
        bind() from factory { userPreference: UserPreferences? -> AbstractGoalViewModelFactory(userPreference, instance()) }
        bind() from factory { userPreference: UserPreferences? -> DietTypeViewModelFactory(userPreference, instance()) }
        bind() from factory { userPreference: UserPreferences? -> IngredientAllergyViewModelFactory(userPreference, instance()) }
        bind() from factory { userPreference: UserPreferences? -> DiseaseViewModelFactory(userPreference, instance(), instance()) }
        bind() from factory { userPreference: UserPreferences? -> DailyNutritionViewModelFactory(userPreference, instance()) }

    }
}