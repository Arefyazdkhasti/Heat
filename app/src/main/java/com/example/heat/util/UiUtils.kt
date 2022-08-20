package com.example.heat.util

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.heat.R
import com.google.android.material.snackbar.Snackbar
import java.security.AccessController.getContext

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

        fun getStringFromResource(context: Context, path:Int):String{
            return context.getString(path)
        }
    }
}