package com.irellia.expenses.helper.Charts

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.model.GradientColor
import com.irellia.expenses.R
import com.irellia.expenses.helper.OpenDialog.openCostDetails
import com.irellia.expenses.model.Cost
import com.irellia.expenses.model.Payment
import java.util.*

/**
 * Created by µðšţãƒâ ™ on 4/18/2019.
 * ->
 */
class BarChartShow(
    private val mBarChart: BarChart,
    private val mAllCost: MutableList<Cost>,
    private val mChildFragmentManager: FragmentManager
) {

    init {
        initChart()
    }

    private fun initChart() {
        mBarChart.setExtraOffsets(5f, 10f, 5f, 5f)
        mBarChart.dragDecelerationFrictionCoef = 0.95f
        mBarChart.setPinchZoom(false)
        mBarChart.setMaxVisibleValueCount(60)
        mBarChart.setDrawGridBackground(true)
        mBarChart.setDrawBarShadow(false)
        mBarChart.setDrawValueAboveBar(true)
        mBarChart.isHighlightPerTapEnabled = true
        mBarChart.legend.isEnabled = false
        mBarChart.axisLeft.setDrawGridLines(false)
        mBarChart.isScaleYEnabled = false
        animateY()

        mBarChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e as BarEntry
                val cost = mAllCost[e.x.toInt()]
                openCostDetails(cost.userId, cost.paymentId).show(mChildFragmentManager, cost.name)
            }
        })
    }

    private fun addXAxis(payments: MutableList<Payment>) {
        val xAxis = mBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = 7
        xAxis.valueFormatter = CostValueFormatter(payments)
    }

    private fun addBarChartData(data: BarData) {
        (mBarChart.context as Activity).runOnUiThread {
            mBarChart.data = data
            mBarChart.highlightValues(null)
            mBarChart.invalidate()
        }
    }

    fun setData() {
        val entries = mutableListOf<BarEntry>()
        val payments = mutableListOf<Payment>()

        mAllCost.forEach {
            payments.add(Payment(name = it.name))
        }

        for (it in mAllCost.indices) {
            entries.add(BarEntry(it.toFloat(), mAllCost[it].price.toFloat()))
        }

        val dataSet = BarDataSet(entries, "")

        dataSet.setDrawIcons(false)

        val startColor1 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_orange_light)
        val startColor2 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_blue_light)
        val startColor3 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_orange_light)
        val startColor4 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_green_light)
        val startColor5 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_red_light)
        val endColor1 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_blue_dark)
        val endColor2 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_purple)
        val endColor3 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_green_dark)
        val endColor4 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_red_dark)
        val endColor5 = ContextCompat.getColor(mBarChart.context, android.R.color.holo_orange_dark)

        val gradientColors = ArrayList<GradientColor>()
        gradientColors.add(GradientColor(startColor1, endColor1))
        gradientColors.add(GradientColor(startColor2, endColor2))
        gradientColors.add(GradientColor(startColor3, endColor3))
        gradientColors.add(GradientColor(startColor4, endColor4))
        gradientColors.add(GradientColor(startColor5, endColor5))

        dataSet.gradientColors = gradientColors

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(dataSet)

        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.barWidth = 0.9f

        if (payments.size > 0)
            addXAxis(payments)

        val month = if (mAllCost.size > 0) mAllCost[0].month else 0
        mBarChart.description.text = String.format(
            mBarChart.context.getString(R.string.expenses_month)
            , mBarChart.context.resources.getStringArray(R.array.months)[month]
        )

        addBarChartData(data)
    }

    fun hideBarChart() {
        mBarChart.visibility = View.GONE
    }

    fun showBarChart() {
        mBarChart.visibility = View.VISIBLE
        animateY()
    }

    private fun animateY() {
        mBarChart.animateY(1500)
    }
}