package com.sict.expenses.helper

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.sict.expenses.R
import com.sict.expenses.model.Cost
import com.sict.expenses.model.Payment
import com.sict.expenses.model.User
import com.sict.expenses.room.RoomDB
import java.util.*

/**
 * Created by µðšţãƒâ ™ on 4/18/2019.
 * ->
 */
class PieChartShow(private val mPieChart: PieChart, private val mUserId: Int) {
    private var mRoomDB: RoomDB = RoomDB.getInstance(mPieChart.context)!!
    private lateinit var mUser: User

    init {
        initChart()
        initThread()
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
        mPieChart.isRotationEnabled = true
        mPieChart.isHighlightPerTapEnabled = true
    }

    private fun initThread() {
        Thread {
            mUser = mRoomDB.userDao().getUser(mUserId)
            setCenterText()
            setData()
        }.start()
    }

    private fun setCenterText() {
        (mPieChart.context as Activity).runOnUiThread {
            mPieChart.centerText = String.format(
                Locale.getDefault(), mPieChart.context.getString(R.string.expenses_month)
                , mPieChart.context.resources.getStringArray(R.array.months)[mUser.month]
            )
        }
    }

    private fun addCharPieData(data: PieData) {
        (mPieChart.context as Activity).runOnUiThread {
            mPieChart.data = data
            mPieChart.highlightValues(null)
            mPieChart.invalidate()
        }
    }

    private fun setData() {
        val entries = mutableListOf<PieEntry>()
        val allCost = mutableListOf<Cost>()
        val payments = mRoomDB.paymentsDao().getAllPaymentsByUser(mUserId)
        var monthTotal = 0.0
        payments.forEach { payment ->
            val mCosts = mRoomDB.costDao().getAllCostChart(mUserId, payment.id!!)
            var total = 0.0
            mCosts.forEach { cost ->
                total += cost.price
            }
            allCost.add(Cost(name = payment.name, price = total))
            monthTotal += total
        }

        allCost.forEach {
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

    fun hidePieChart(){
        mPieChart.visibility = View.GONE
    }

    fun showPieChart(){
        mPieChart.visibility = View.VISIBLE
    }
}