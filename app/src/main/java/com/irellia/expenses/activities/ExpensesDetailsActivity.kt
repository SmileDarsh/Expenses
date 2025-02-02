package com.irellia.expenses.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import com.irellia.expenses.R
import com.irellia.expenses.base.BaseActivity
import com.irellia.expenses.helper.OpenDialog.openAddExpenses
import com.irellia.expenses.helper.Utils.dateFormat
import com.irellia.expenses.helper.Utils.numberFormat
import com.irellia.expenses.model.Cost
import com.irellia.expenses.model.Expenses
import com.irellia.expenses.model.ExpensesDetails
import com.irellia.expenses.popupDialog.AddExpensesDialog
import kotlinx.android.synthetic.main.activity_expenses_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
class ExpensesDetailsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mExpenses: Expenses
    private val mCostList = mutableListOf<Cost>()
    private val mEditTextList = mutableListOf<ExpensesDetails>()
    override fun loadLayoutResource(): Int = R.layout.activity_expenses_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mExpenses = intent.getSerializableExtra("expenses") as Expenses

        toolbar.title = dateFormat(mExpenses.date)

        ibnEdit.setOnClickListener(this)
        tvSave.setOnClickListener(this)
        btnAdd.setOnClickListener(this)

        Thread {
            mCostList.addAll(mRoomDB.costDao().getCostByExpenses(mExpenses.id!!))
            if (mCostList.size > 0)
                mCostList.forEach { addItem(it) }
            else {
                mRoomDB.expensesDao().deleteExpensesByUser(mExpenses.id!!)
                finish()
            }
        }.start()
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
     * Come from [AddExpensesDialog.costNameIsReadyExist]
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCostCreate(cost: Cost) {
        if (updateItem(cost))
            addItem(cost)
        saveCost()
    }

    private fun addItem(cost: Cost) {
        runOnUiThread {
            val view = LayoutInflater.from(this).inflate(R.layout.custom_add_expenses, null, false)
            val etPaymentValue: EditText = view.findViewById(R.id.etPaymentValue)
            val tvPaymentName: TextView = view.findViewById(R.id.tvPaymentName)

            addCost(cost)
            etPaymentValue.isEnabled = false
            tvPaymentName.text = cost.name
            etPaymentValue.setText(numberFormat(cost.price))
            mEditTextList.add(ExpensesDetails(etPaymentValue, tvPaymentName))
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 16, 0, 16)
            view.layoutParams = params
            llPayments.addView(view)
        }
    }

    private fun updateItem(cost: Cost): Boolean {
        for (it in mCostList.indices) {
            if (cost.id == mCostList[it].id && !mCostList[it].active) {
                mCostList[it].active = true
                mCostList[it].price = cost.price
                mEditTextList[it].value!!.setText(cost.price.toString())
                return false
            }
        }
        return true
    }

    private fun addCost(cost: Cost) {
        var isExist = false
        mCostList.forEach {
            if (cost.id == it.id) {
                isExist = true
                return
            }
        }
        if (!isExist)
            mCostList.add(cost)
    }

    private fun saveCost() {
        var allCost = 0.0
        Thread {
            for (it in mEditTextList.indices) {
                val cost = mCostList[it]
                if (it in mEditTextList.indices) {
                    if (mEditTextList[it].value!!.text.isNotEmpty()) {
                        cost.price = mEditTextList[it].value!!.text.toString().trim().toDouble()
                    } else {
                        cost.price = 0.0
                    }

                    if (cost.price == 0.0)
                        cost.active = false
                    mRoomDB.costDao().updateCost(cost)
                    allCost += mCostList[it].price
                    mExpenses.price = allCost
                    mRoomDB.expensesDao().updateExpenses(mExpenses)
                }
            }
        }.start()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            ibnEdit.id -> {
                mEditTextList.forEach { it.value!!.isEnabled = true }
                showSave()
            }
            tvSave.id -> {
                mEditTextList.forEach { it.value!!.isEnabled = false }
                saveCost()
                hideSave()
            }
            btnAdd.id -> {
                openAddExpenses(mExpenses.userId, mExpenses.date).show(supportFragmentManager, "expensesDetails")
            }
        }
    }

    private fun showSave() {
        tvSave.visibility = View.VISIBLE
        ibnEdit.visibility = View.GONE
        btnAdd.visibility = View.GONE
    }

    private fun hideSave() {
        tvSave.visibility = View.GONE
        ibnEdit.visibility = View.VISIBLE
        btnAdd.visibility = View.VISIBLE
    }
}