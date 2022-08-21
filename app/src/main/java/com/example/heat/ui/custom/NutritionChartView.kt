package com.example.heat.ui.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.heat.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class  NutritionChartView(
    contextInstant: Context,
    attrs: AttributeSet
) : FrameLayout(contextInstant, attrs) {

    private val itemTitle: AppCompatTextView
    private val itemIcon: AppCompatImageView
    private val pieChart: PieChart

    init {
        val view = inflate(context, R.layout.nutrition_chart_view, this)

        //get attrs
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NutritionChartView)
        val title = typedArray.getString(R.styleable.NutritionChartView_name)
        //TODO set icon
        //val icon = typedArray.getInt(R.styleable.NutritionChartView_icon,0)
        //recycle type array after use
        typedArray.recycle()


        itemTitle = view.findViewById(R.id.chart_view_nutrition_name)
        itemIcon = view.findViewById(R.id.chart_view_nutrition_icon)
        pieChart = view.findViewById(R.id.nutrition_chart)

        itemTitle.text = title
        //itemIcon.setBackgroundResource(icon)
    }


    fun initPieChart(entries: ArrayList<PieEntry>) {
        val colors: ArrayList<Int> = ArrayList()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }

        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }

        val dataSet = PieDataSet(entries,"")
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        pieChart.data = data
        pieChart.invalidate()

        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }
}