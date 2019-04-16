package com.sict.expenses.popupDialog

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sict.expenses.R
import com.sict.expenses.base.BaseDialogFragment
import com.sict.expenses.helper.Logging
import com.sict.expenses.model.Payment
import kotlinx.android.synthetic.main.dialog_add_payment.*

/**
 * Created by µðšţãƒâ ™ on 4/11/2019.
 * ->
 */
class AddPaymentDialog : BaseDialogFragment(), View.OnClickListener {
    private lateinit var mAdapter: ArrayAdapter<String>
    private val mPaymentsNames = mutableListOf<String>()

    override fun loadLayoutResource(): Int = R.layout.dialog_add_payment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAutoComplete()

        btnAdd.setOnClickListener(this)
        btnRemove.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            btnAdd.id -> {
                if (validate()) {
                    Thread {
                        if (mRoomDB.paymentsDao().isPayment(etPaymentName.text.toString().trim()) == 0) {
                            mRoomDB.paymentsDao().insertPayment(Payment(name = etPaymentName.text.toString()))
                            mAdapter.add(etPaymentName.text.toString())
                            activity!!.runOnUiThread {
                                etPaymentName.text.clear()
                                Logging.toast(context!!, R.string.add_successful)
                            }
                        } else {
                            val payment = mRoomDB.paymentsDao().getPayment(etPaymentName.text.toString())
                            if (!payment.active) {
                                payment.active = true
                                mRoomDB.paymentsDao().updatePayment(payment)
                                mAdapter.add(etPaymentName.text.toString())
                                activity!!.runOnUiThread {
                                    etPaymentName.text.clear()
                                    Logging.toast(context!!, R.string.add_successful)
                                }
                            } else
                                showError(etPaymentName, getString(R.string.toast_payment_exist))
                        }
                    }.start()
                }
            }
            btnRemove.id -> {
                if (validate()) {
                    Thread {
                        if (mRoomDB.paymentsDao().isPayment(etPaymentName.text.toString().trim()) > 0) {
                            val payment = mRoomDB.paymentsDao().getPayment(etPaymentName.text.toString())
                            payment.active = false
                            mRoomDB.paymentsDao().updatePayment(payment)
                            mAdapter.remove(etPaymentName.text.toString())
                            activity!!.runOnUiThread {
                                etPaymentName.text.clear()
                                Logging.toast(context!!, R.string.remove_successful)
                            }
                        } else {
                            showError(etPaymentName, getString(R.string.toast_payment_not_exist))
                        }
                    }.start()
                }
            }
            btnCancel.id -> dismiss()
        }
    }

    private fun initAutoComplete() {
        Thread {
            mPaymentsNames.addAll(mRoomDB.paymentsDao().getPaymentNames())
            mAdapter =
                ArrayAdapter(context!!, R.layout.spinner_item, mPaymentsNames)
            activity!!.runOnUiThread {
                etPaymentName.threshold = 2
                etPaymentName.setAdapter(mAdapter)
            }
        }.start()
    }

    private fun showError(v: TextView, error: String?) {
        activity!!.runOnUiThread { v.error = error }
    }

    private fun validate(): Boolean {
        return if (etPaymentName.text.toString().trim().isNotEmpty()) {
            showError(etPaymentName, null)
            true
        } else {
            showError(etPaymentName, getString(R.string.toast_empty_field))
            false
        }
    }
}
