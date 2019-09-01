package com.irellia.expenses.fragment

import android.os.Bundle
import android.view.View
import com.irellia.expenses.R
import com.irellia.expenses.base.BaseFragment
import com.irellia.expenses.helper.Charts.BarChartShow
import com.irellia.expenses.helper.Charts.PieChartShow
import com.irellia.expenses.model.Cost
import kotlinx.android.synthetic.main.fragment_cost.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

/**
 * Created by µðšţãƒâ ™ on 4/15/2019.
 * ->
 */
class CostFragment : BaseFragment() {
    private lateinit var mPieChart: PieChartShow
    private lateinit var mBarChart: BarChartShow
    private val mAllCost = mutableListOf<Cost>()
    override fun loadLayoutResource(): Int = R.layout.fragment_cost

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPieChart = PieChartShow(pcCosts, mAllCost, childFragmentManager)
        mBarChart = BarChartShow(bcCosts, mAllCost, childFragmentManager)
        setChartData()
        btnChart.setOnClickListener {
            showChartButton()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    /**
     * Come from [ChooseMonth] [CostDetailsDialog]
     * When user change Month from ChooseMonthDialog
     * and When user click any value on chart disable another item click
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWalletChanged(change: String) {
        if (change == "change")
            setChartData()
        else if(change == "destroy") {
            mPieChart.onChartClickable(true)
            mBarChart.onChartClickable(true)
        }
    }

    private fun setChartData() {
        Thread {
            mAllCost.clear()
            val payments = mRoomDB.paymentsDao().getAllPaymentsByUser(mUserId)
            var monthTotal = 0.0
            payments.forEach { payment ->
                val costs = mRoomDB.costDao().getAllCostChart(mUserId, payment.id!!)
                if (costs.size > 0) {
                    var total = 0.0
                    costs.forEach { cost ->
                        total += cost.price
                    }
                    mAllCost.add(
                        Cost(
                            month = costs[0].month,
                            userId = mUserId,
                            paymentId = payment.id!!,
                            name = payment.name,
                            price = total
                        )
                    )
                    monthTotal += total
                }
            }
            mPieChart.setData(monthTotal)
            mBarChart.setData()
            try {
                pcCosts.notifyDataSetChanged()
            }catch (ex : Exception){
                ex.printStackTrace()
            }
            bcCosts.notifyDataSetChanged()
            checkList(mAllCost.size)
        }.start()
    }

    private fun checkList(size: Int) {
        activity!!.runOnUiThread {
            if (size == 0) {
                tvNoData.visibility = View.VISIBLE
                btnChart.visibility = View.GONE
                mPieChart.hidePieChart()
                mBarChart.hideBarChart()

            } else {
                tvNoData.visibility = View.GONE
                btnChart.visibility = View.VISIBLE
                showChart()
            }
        }
    }

    private fun showChartButton() {
        if (btnChart.text.toString() == getString(R.string.bar_chart)) {
            btnChart.text = getString(R.string.pie_chart)
            mPieChart.hidePieChart()
            mBarChart.showBarChart()
        } else {
            btnChart.text = getString(R.string.bar_chart)
            mPieChart.showPieChart()
            mBarChart.hideBarChart()
        }
    }

    private fun showChart() {
        if (btnChart.text.toString() == getString(R.string.bar_chart))
            mPieChart.showPieChart()
        else
            mBarChart.showBarChart()
    }
}