package com.irellia.expenses.helper.Charts

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.fragment.app.FragmentManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.irellia.expenses.R
import com.irellia.expenses.helper.OpenDialog.openCostDetails
import com.irellia.expenses.model.Cost
import java.util.*

/**
 * Created by µðšţãƒâ ™ on 4/18/2019.
 * ->
 */
class PieChartShow(
    private val mPieChart: PieChart, private val mAllCost: MutableList<Cost>,
    private val mChildFragmentManager: FragmentManager
) {
    init {
        initChart()
    }

    private fun initChart() {
        mPieChart.setUsePercentValues(true)
        mPieChart.description.isEnabled = false
        mPieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        mPieChart.dragDecelerationFrictionCoef = 0.95f
        mPieChart.isDrawHoleEnabled = true
        mPieChart.setHoleColor(Color.WHITE)
        mPieChart.setTransparentCircleColor(Color.WHITE)
        mPieChart.setTransparentCircleAlpha(110)
        mPieChart.holeRadius = 58f
        mPieChart.transparentCircleRadius = 61f
        mPieChart.setDrawCenterText(true)
        mPieChart.rotationAngle = 0f
        mPieChart.isRotationEnabled = false
        mPieChart.isHighlightPerTapEnabled = true
        mPieChart.setDrawEntryLabels(false)
        animateY()

        mPieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e as PieEntry
                val cost = mAllCost.single { it.name == e.label }
                openCostDetails(cost.userId, cost.paymentId).show(mChildFragmentManager, e.label)
                onChartClickable(false)
            }
        })
    }

    private fun setCenterText(month: Int) {
        mPieChart.centerText = String.format(
            Locale.getDefault(), mPieChart.context.getString(R.string.expenses_month)
            , mPieChart.context.resources.getStringArray(R.array.months)[month]
        )
    }

    private fun addCharPieData(data: PieData) {
        (mPieChart.context as Activity).runOnUiThread {
            try {
                mPieChart.data = data
                mPieChart.highlightValues(null)
                mPieChart.invalidate()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun setData(monthTotal: Double) {
        if (monthTotal > 0.0)
            setCenterText(mAllCost[0].month)

        val entries = mutableListOf<PieEntry>()

        mAllCost.forEach {
            entries.add(PieEntry(((it.price / monthTotal) * 100).toFloat(), it.name))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors = mutableListOf<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(mPieChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)

        addCharPieData(data)
    }

    fun hidePieChart() {
        mPieChart.visibility = View.GONE
    }

    fun showPieChart() {
        mPieChart.visibility = View.VISIBLE
        animateY()
    }

    fun onChartClickable(enable: Boolean) {
        mPieChart.setTouchEnabled(enable)
    }

    private fun animateY() {
        mPieChart.animateY(1500)
    }
}