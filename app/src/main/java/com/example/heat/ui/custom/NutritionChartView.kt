package com.example.heat.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.heat.R
import com.example.heat.util.UiUtils.Companion.getColor
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.math.roundToInt

class  NutritionChartView(
    contextInstant: Context,
    attrs: AttributeSet
) : FrameLayout(contextInstant, attrs) {

    private val layout : ConstraintLayout
    private val itemTitle: AppCompatTextView
    private val itemIcon: AppCompatImageView
    private lateinit var pieChart: ProgressBar
    private val percentage: AppCompatTextView

    private val CALORIES = "calories"
    private val CARBO = "carbo"
    private val FAT = "fat"
    private val PROTEIN = "protein"

    init {
        val view = inflate(context, R.layout.nutrition_chart_view, this)

        //get attrs
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NutritionChartView)
        val title = typedArray.getString(R.styleable.NutritionChartView_name)
        val type = typedArray.getString(R.styleable.NutritionChartView_type)
        //TODO set icon
        //val icon = typedArray.getInt(R.styleable.NutritionChartView_icon,0)
        //recycle type array after use
        typedArray.recycle()
        layout = view.findViewById(R.id.nutrition_chart_view_root_constraint)
        itemTitle = view.findViewById(R.id.chart_view_nutrition_name)
        itemIcon = view.findViewById(R.id.chart_view_nutrition_icon)
        pieChart = view.findViewById(R.id.progress_bar_nutrition)
        percentage = view.findViewById(R.id.progress_percentage_nutrition)

        itemTitle.text = title
        //itemIcon.setBackgroundResource(icon)

        when(type){
            CARBO -> {
                //pieChart = ProgressBar(context,null, R.style.CircularDeterminateProgressBarCarbo)
                percentage.setTextColor(getColor(context, R.color.carbo_red))
            }
            FAT -> {
                //pieChart = ProgressBar(context, null, R.style.CircularDeterminateProgressBarFat)
                percentage.setTextColor(getColor(context, R.color.fat_yellow))
            }
            PROTEIN -> {
                //pieChart = ProgressBar(context, null, R.style.CircularDeterminateProgressBarProtein)
                percentage.setTextColor(getColor(context, R.color.protein_blue))
            }
            CALORIES -> {
                //pieChart = ProgressBar(context, null, R.style.CircularDeterminateProgressBarCalories)
                percentage.setTextColor(getColor(context, R.color.calories_green))
            }
        }
        //layout.addView(pieChart)
    }


    @SuppressLint("SetTextI18n")
    fun initChart(percent: Int) {
        pieChart.progress = percent
        percentage.text = "$percent%"
    }

}