package com.example.heat.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceManager
import com.example.heat.BuildConfig
import com.example.heat.R
import com.example.heat.data.datamodel.DayListItem
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager





class UiUtils {
    companion object {


        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

        fun setTypeFace(context: Context, textView: AppCompatTextView) {
            val typeface = Typeface.createFromAsset(
                context.assets,
                "D:/Android/Heat/app/src/main/res/font/regular.ttf"
            )
            textView.typeface = typeface
        }

        fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun showToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        fun showSimpleSnackBar(view: View, text: String) {
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
        }

        fun setImageWithGlideWithView(view: View, url: String, imageView: ImageView) {
            GlideApp.with(view)
                .load(url)
                .placeholder(R.drawable.healthy_food_placeholder)
                .into(imageView)
        }

        fun setImageWithGlideWithContext(context: Context, url: String, imageView: ImageView) {
            GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.healthy_food_placeholder)
                .into(imageView)
        }

        //fun getStringFromResource( path:Int):String =  ContextCompat.getS

        // extension function to get a string resource by resource name
        fun Context.stringFromResourcesByName(resourceName: String): String {
            return try {
                // get the string resource id by name
                val resourceId = resources.getIdentifier(
                    resourceName,
                    "string",
                    packageName
                )

                // return the string value
                getString(resourceId)
            } catch (e: Resources.NotFoundException) {
                ""
            }
        }

        fun getURLForResource(resourceId: Int): String {
            //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
            return Uri.parse(
                "android.resource://" + BuildConfig.APPLICATION_ID + "/" + resourceId
            ).toString()
        }

        fun isEditTextEmpty(editText: TextInputEditText): Boolean =
            TextUtils.isEmpty(editText.text.toString())

        fun getColor(context: Context, res: Int) = ContextCompat.getColor(context, res)

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDate(day: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(day, formatter)

            val dayFormatter = DateTimeFormatter.ofPattern("dd")
            val dayFormatted = LocalDate.parse(day, dayFormatter)

            val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
            val yearFormatted = LocalDate.parse(day, yearFormatter)

            return "${date.dayOfWeek} $dayFormatted ${date.month} $yearFormatted"
        }

        @DelicateCoroutinesApi
        fun saveUserLoginStatusToDataStore(context: Context, login: Boolean) {
            val dataStore = context.dataStore
            val userManager = UserManager(dataStore)
            GlobalScope.launch {
                userManager.storeUser(login)
            }
        }

        @DelicateCoroutinesApi
        fun saveUserIDToDataStore(context: Context, id:Int) {
            val dataStore = context.dataStore
            val userManager = UserIDManager(dataStore)
            GlobalScope.launch {
                userManager.storeUserID(id)
            }
        }

        fun getDayOrWeekFromSetting(context: Context): String{
            val preference = PreferenceManager.getDefaultSharedPreferences(context)
            val selectedType = preference.getString("mealPlan" , "one_week_plan")
            return selectedType.toString()
        }

        fun getCurrentDate(): String {

            val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return current.format(formatter)
        }

        fun checkForInternet(activity: Activity,lifecycle: Lifecycle): Boolean {

            var isConnected = false

            // No Internet Dialog
            var noInternetDialog = NoInternetDialogPendulum.Builder(
                activity,
                lifecycle
            ).apply {
                dialogProperties.apply {
                    connectionCallback = object : ConnectionCallback { // Optional
                        override fun hasActiveConnection(hasActiveConnection: Boolean) {
                            isConnected = hasActiveConnection
                        }
                    }

                    cancelable = false // Optional
                    noInternetConnectionTitle = "No Internet" // Optional
                    noInternetConnectionMessage =
                        "Check your Internet connection and try again." // Optional
                    showInternetOnButtons = true // Optional
                    pleaseTurnOnText = "Please turn on" // Optional
                    wifiOnButtonText = "Wifi" // Optional
                    mobileDataOnButtonText = "Mobile data" // Optional

                    onAirplaneModeTitle = "No Internet" // Optional
                    onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
                    pleaseTurnOffText = "Please turn off" // Optional
                    airplaneModeOffButtonText = "Airplane mode" // Optional
                    showAirplaneModeOffButtons = true // Optional
                }
            }.build()

            return  isConnected
        }

        fun isNetworkConnected(activity:Activity): Boolean {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (cm != null) {
                return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
            }
            return false
        }
    }
}

interface SendEvent {
    fun sendCheckedStatus(check: Boolean, meal: FoodSummery)
    fun sendWholeCheckedStatus(check: Boolean, day: DayListItem)
    fun sendOneMealUnChecked()
    fun regenerateOneMeal(meal: FoodSummery)
    fun regenerateWholePlan(plan: DayListItem)
}