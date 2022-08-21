package com.example.heat.util

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.snackbar.Snackbar
import android.net.Uri
import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import androidx.core.content.res.TypedArrayUtils.getString
import com.example.heat.BuildConfig
import com.example.heat.R


class UiUtils {
    companion object {

        fun setTypeFace(context: Context, textView: AppCompatTextView) {
            val typeface = Typeface.createFromAsset(context.assets, "D:/Android/Heat/app/src/main/res/font/regular.ttf")
            textView.typeface = typeface
        }

        fun showToast(context: Context, text: String) {
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
        }

        fun showSimpleSnackBar(view: View, text: String) {
            Snackbar.make(view,text,Snackbar.LENGTH_SHORT).show()
        }

        fun setImageWithGlideWithView(view:View,  url:String, imageView: ImageView) {
            GlideApp.with(view)
                .load(url)
                .placeholder(R.drawable.load)
                .into(imageView)
        }
        fun setImageWithGlideWithContext(context: Context,  url:String, imageView: ImageView) {
            GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.load)
                .into(imageView)
        }

        fun getStringFromResource( path:Int):String =   /*Resources.getSystem().getString(path)*/ "test"

        fun getURLForResource(resourceId: Int): String {
            //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
            return Uri.parse(
                "android.resource://" +  BuildConfig.APPLICATION_ID+ "/" + resourceId
            ).toString()
        }
    }
}