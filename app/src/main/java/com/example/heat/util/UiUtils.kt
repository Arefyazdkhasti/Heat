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
import android.provider.Settings.System.getString
import android.text.TextUtils
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getString
import com.example.heat.BuildConfig
import com.example.heat.R
import com.example.heat.data.datamodel.EatenMealItem
import com.example.heat.data.datamodel.MealListItem
import com.google.android.material.textfield.TextInputEditText


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
            }catch (e:Resources.NotFoundException){
                ""
            }
        }
        fun getURLForResource(resourceId: Int): String {
            //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
            return Uri.parse(
                "android.resource://" +  BuildConfig.APPLICATION_ID+ "/" + resourceId
            ).toString()
        }

        fun isEditTextEmpty(editText: TextInputEditText):Boolean = TextUtils.isEmpty(editText.text.toString())

        fun getColor(context:Context, res:Int) = ContextCompat.getColor(context,res)

    }
}

interface SendEvent{
    fun sendCheckedStatus(check:Boolean, meal: MealListItem)
}