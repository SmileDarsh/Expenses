package com.sict.expenses.popupDialog

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sict.expenses.R
import com.sict.expenses.adapter.CostDetailsAdapter
import com.sict.expenses.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_cost_details.*
import kotlinx.android.synthetic.main.item_total.view.*

/**
 * Created by µðšţãƒâ ™ on 4/21/2019.
 * ->
 */
class CostDetailsDialog : BaseDialogFragment() {
    private lateinit var mAdapter: CostDetailsAdapter
    private var mUserId: Int = 0
    private var mPaymentId: Int = 0
    override fun loadLayoutResource(): Int = R.layout.dialog_cost_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mWidth = .95
        mHeight = .85

        mUserId = arguments!!.getInt("userId")
        mPaymentId = arguments!!.getInt("paymentId")

        Thread {
            mAdapter = CostDetailsAdapter(mRoomDB.costDao().getAllCostChart(mUserId, mPaymentId))
            initExpensesRecyclerView()
        }.start()

        tvTitle.text = tag
        btnCancel.setOnClickListener { dismiss() }
    }

    private fun initExpensesRecyclerView() {
        activity!!.runOnUiThread {
            rvCosts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
                vTotal.tvValueOne.text = mAdapter.getAllCostPrice()
            }
        }
    }
}