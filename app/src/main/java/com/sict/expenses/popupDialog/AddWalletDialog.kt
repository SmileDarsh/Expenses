package com.sict.expenses.popupDialog

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.sict.expenses.R
import com.sict.expenses.base.BaseDialogFragment
import com.sict.expenses.helper.Logging
import com.sict.expenses.helper.Utils
import com.sict.expenses.model.User
import com.sict.expenses.model.Wallet
import kotlinx.android.synthetic.main.dialog_add_wallet.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
class AddWalletDialog : BaseDialogFragment() {
    private var mUserId = 0
    private lateinit var mUser: User
    private var mWallet: Wallet? = null
    private var mFirstTime = false

    override fun loadLayoutResource(): Int = R.layout.dialog_add_wallet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHeight = .40
        mUserId = arguments!!.getInt("userId")


        Thread {
            mUser = mRoomDB.userDao().getUser(mUserId)
            mWallet = mRoomDB.walletDao().getWallet(mUser.id!!)
            mFirstTime = if (mWallet == null) {
                true
            } else {
                addText(tvWallet, String.format(getString(R.string.your_wallet), mWallet!!.value.toString()))
                mWallet!!.value == 0.0
            }
            addText(etWallet, "0.0")
            addText(tvDate, Utils.dateFormat(context!!, mUser.month, mUser.year))
        }.start()


        etWallet.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE)
                addWallet()
            return@setOnEditorActionListener false
        }

        btnAdd.setOnClickListener {
            addWallet()
        }
    }

    private fun addWallet() {
        if (etWallet.text.toString().trim().isNotEmpty() && etWallet.text.toString().trim() != "0.0")
            Thread {
                val wallet = etWallet.text.toString().toDouble()
                if (mFirstTime && mWallet == null) {
                    addNewWallet(wallet)
                } else {
                    if (validate()) {
                        mWallet!!.value += wallet
                        mRoomDB.walletDao().updateWallet(mWallet!!)
                    } else {
                        addNewWallet(wallet)
                    }
                }

                val wallets = mRoomDB.walletDao().getAllWallet()
                wallets.forEach {
                    Logging.log("Wallet : ${it.id} - ${it.userId} - ${it.month} - ${it.year} - ${it.value}")
                }
                if (tag == "login")
                    EventBus.getDefault().post("finish")
                EventBus.getDefault().post(mWallet)
                dismiss()
            }.start()
        else
            etWallet.error = getString(R.string.toast_empty_field)
    }

    private fun validate() = mWallet!!.month == mUser.month && mWallet!!.year == mUser.year

    private fun addNewWallet(wallet: Double) {
        mWallet = mRoomDB.walletDao().getWalletById(
            mRoomDB.walletDao().insertWallet(
                Wallet(
                    userId = mUser.id!!,
                    month = mUser.month,
                    year = mUser.year,
                    value = wallet
                )
            ).toInt()
        )
    }

    private fun addText(tv: TextView, text: CharSequence) {
        activity!!.runOnUiThread { tv.text = text }
    }
}