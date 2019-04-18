package com.sict.expenses.popupDialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import com.sict.expenses.R
import com.sict.expenses.base.BaseDialogFragment
import com.sict.expenses.helper.Logging
import com.sict.expenses.helper.Utils
import com.sict.expenses.model.*
import kotlinx.android.synthetic.main.dialog_add_expenses.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
class AddExpensesDialog : BaseDialogFragment() {
    private val mPaymentsList = mutableListOf<Payment>()
    private lateinit var mTvDate: TextView
    private lateinit var mEtPaymentName: EditText
    private lateinit var mEtPaymentValue: EditText
    private lateinit var mSpPayment: AppCompatSpinner
    private lateinit var mUser: User
    private var mDate: Long = 0L
    private var mDay: Int = 0
    private var mMonth: Int = 0
    private var mYear: Int = 0
    private var updateExpenses = false
    private var mPaymentId = 0

    override fun loadLayoutResource(): Int = R.layout.dialog_add_expenses

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHeight = .55
        with(view) {
            mTvDate = tvDate
            mEtPaymentName = etPaymentName
            mEtPaymentValue = etPaymentValue
            mSpPayment = spPayments

            btnAdd.setOnClickListener { addExpenses() }
        }

        mDate = arguments!!.getLong("date", 0L)
        val userId = arguments!!.getInt("userId")

        Thread {
            mUser = mRoomDB.userDao().getUser(userId)
            mMonth = mUser.month
            mYear = mUser.year
        }.start()

        openDatePacker()
        paymentSpinner()
    }

    private fun openDatePacker() {
        if (mDate > 0) {
            mTvDate.isEnabled = false
            updateExpenses = true
        } else
            mDate = Date().time

        mTvDate.text = Utils.dateFormat(mDate)
        mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        mTvDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    mDate = Utils.getLongDateFromDatePicker(view)
                    mTvDate.text = Utils.dateFormat(mDate)
                    mDay = day
                    mMonth = month
                    mYear = year
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }

    private fun paymentSpinner() {
        Thread {
            val payments = mutableListOf<CharSequence>()
            mPaymentsList.addAll(mRoomDB.paymentsDao().getAllPayments())
            activity!!.runOnUiThread {
                payments.add(getString(R.string.new_payment))
                mPaymentsList.forEach { payments.add(it.name) }
                val adapter = ArrayAdapter<CharSequence>(context!!, R.layout.spinner_item, payments)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mSpPayment.adapter = adapter
                if (mPaymentsList.size > 0)
                    mSpPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            mEtPaymentName.text.clear()
                            mEtPaymentName.visibility = View.VISIBLE
                            if (position > 0)
                                mEtPaymentName.visibility = View.GONE
                        }
                    }
            }
        }.start()
    }

    private fun validate(): Boolean {
        var cnt = 0
        if (mEtPaymentValue.text.toString().trim().isEmpty()) {
            showError(mEtPaymentValue, getString(R.string.toast_empty_field))
            cnt++
        } else
            showError(mEtPaymentValue, null)

        if (mRoomDB.expensesDao().isExist(mUser.id!!, mTvDate.text.toString()) != 0 && !updateExpenses) {
            showError(mTvDate, getString(R.string.toast_date_error))
            cnt++
        } else
            showError(mTvDate, null)

        if (mSpPayment.selectedItemPosition == 0 && mEtPaymentName.text.trim().isEmpty()) {
            showError(mEtPaymentName, getString(R.string.toast_empty_field))
            cnt++
        } else
            showError(mEtPaymentName, null)

        return cnt == 0
    }

    private fun showError(v: TextView, error: String?) {
        activity!!.runOnUiThread { v.error = error }
    }

    private fun addExpenses() {
        Thread {
            mPaymentId = if (mEtPaymentName.text.toString().trim().isNotEmpty() &&
                mRoomDB.paymentsDao().isPayment(mEtPaymentName.text.toString().trim()) == 0
            ) {
                mRoomDB.paymentsDao().insertPayment(Payment(name = mEtPaymentName.text.toString().trim())).toInt()
            } else {
                if (mSpPayment.selectedItemPosition == 0) {
                    mRoomDB.paymentsDao().getPayment(mEtPaymentName.text.toString().trim()).id!!
                } else {
                    mPaymentsList[mSpPayment.selectedItemPosition - 1].id!!
                }
            }

            if (validate()) {
                if (!updateExpenses)
                    addNewExpenses()
                addNewCost()

                dialog.dismiss()
            }
        }.start()
    }

    private fun getCost(): String {
        return if (mSpPayment.selectedItemPosition != 0 && mEtPaymentName.text.trim().isEmpty()) {
            mPaymentsList[mSpPayment.selectedItemPosition - 1].name
        } else {
            mEtPaymentName.text.toString()
        }
    }

    private fun addNewExpenses() {
        val expenses = Expenses(
            userId = mUser.id!!,
            date = mDate,
            isDate = mTvDate.text.toString(),
            month = mMonth,
            year = mYear,
            price = mEtPaymentValue.text.toString().toDouble()
        )
        mRoomDB.expensesDao().insertExpenses(expenses)
    }

    private fun addNewCost() {
        val expenses = mRoomDB.expensesDao().getExpensesId(mUser.id!!, mTvDate.text.toString())
        val cost = Cost(
            userId = expenses.userId,
            pExpensesId = expenses.id!!, name = getCost(),
            paymentId = mPaymentId,
            price = mEtPaymentValue.text.toString().toDouble(),
            date = mDate,
            day = mDay,
            month = expenses.month,
            year = expenses.year
        )
        costNameIsReadyExist(expenses.id!!, cost)
        if (mRoomDB.fkPaymentDao().isNotExist(mUser.id!!, mPaymentId) == 0)
            mRoomDB.fkPaymentDao().insertFKPayment(FKPayment(userId = mUser.id!!, paymentId = mPaymentId))
    }

    private fun costNameIsReadyExist(parentId: Int, cost: Cost) {
        Thread {
            if (mRoomDB.costDao().nameIsExist(parentId, cost.paymentId) == 0) {
                val costId = mRoomDB.costDao().insertCost(cost)
                cost.id = costId.toInt()
                EventBus.getDefault().post(cost)
            } else {
                val costUpdate = mRoomDB.costDao().getCost(parentId, cost.paymentId)
                if (costUpdate.active) {
                    activity!!.runOnUiThread { Logging.toast(context!!, R.string.toast_payment_exist) }
                } else {
                    costUpdate.price = cost.price
                    EventBus.getDefault().post(costUpdate)
                }
            }
        }.start()
    }
}