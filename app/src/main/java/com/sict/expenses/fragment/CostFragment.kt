package com.sict.expenses.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sict.expenses.R
import com.sict.expenses.adapter.CostAdapter
import com.sict.expenses.base.BaseFragment
import com.sict.expenses.base.CostViewModelFactory
import com.sict.expenses.model.Cost
import com.sict.expenses.model.Payment
import com.sict.expenses.viewModel.CostViewModel
import kotlinx.android.synthetic.main.fragment_cost.*
import kotlinx.android.synthetic.main.item_total.*

/**
 * Created by µðšţãƒâ ™ on 4/15/2019.
 * ->
 */
class CostFragment : BaseFragment() {
    private lateinit var vm: CostViewModel
    private val mPaymentsList = mutableListOf<Payment>()
    private val mAdapter = CostAdapter()
    private var mPaymentId = 0
    override fun loadLayoutResource(): Int = R.layout.fragment_cost

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentSpinner()

        vm = ViewModelProviders.of(
            activity!!, CostViewModelFactory(activity!!.application, mUserId, mPaymentId)
        ).get(CostViewModel::class.java)

        initExpensesRecyclerView()

        startListening()
    }

    private fun initExpensesRecyclerView() {
        rvCosts.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun paymentSpinner() {
        Thread {
            val payments = mutableListOf<CharSequence>()
            mPaymentsList.addAll(mRoomDB.paymentsDao().getAllPayments())
            activity!!.runOnUiThread {
                payments.add(getString(R.string.choose_payment))
                mPaymentsList.forEach { payments.add(it.name) }
                val adapter = ArrayAdapter<CharSequence>(context!!, R.layout.spinner_center_item, payments)
                adapter.setDropDownViewResource(R.layout.spinner_center_item)
                spPayments.adapter = adapter
                if (mPaymentsList.size > 0)
                    spPayments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            if (position != 0)
                                updatePaymentId(position)
                        }
                    }
            }
        }.start()
    }

    private fun updatePaymentId(position: Int) {
        mPaymentId = mPaymentsList[position - 1].id!!
        Thread {
            val cost: Cost? = mRoomDB.costDao().getCost(1, mPaymentId)
            if (cost != null)
                mRoomDB.costDao().updateCost(cost)
            replaceSubscription()
        }.start()
    }

    private fun startListening() {
        vm.costList!!.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

    private fun replaceSubscription() {
        activity!!.runOnUiThread {
            vm.replaceSubscription(this, mPaymentId)
            startListening()
        }
    }
}