package com.example.heat.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.widget.SearchView
import com.example.heat.BuildConfig
import java.text.NumberFormat
import java.util.*


val <T> T.exhaustive: T
    get() = this

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}
fun View.startMyAnimation(animation: Animation, onEnd: () -> Unit) {
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(animation: Animation?) = Unit
    })
    this.startAnimation(animation)
}

fun getURLForResource(resourceId: Int): String {
    //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
    return Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + resourceId)
        .toString()
}

fun setLocale(activity: Activity, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources = activity.resources
    val config: Configuration = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun drawbleToString(context: Context) {

    val id: Int = context.resources.getIdentifier("picture0001", "drawable", context.packageName)
}

fun moneyFormatter(number: Long): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("IRR")

    return format.format(number)
}