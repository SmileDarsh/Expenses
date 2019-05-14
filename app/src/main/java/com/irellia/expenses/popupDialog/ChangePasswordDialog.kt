package com.irellia.expenses.popupDialog

import android.os.Bundle
import android.view.View
import com.irellia.expenses.R
import com.irellia.expenses.base.BaseDialogFragment
import com.irellia.expenses.helper.Logging
import com.irellia.expenses.model.User
import kotlinx.android.synthetic.main.dialog_change_password.*

/**
 * Created by µðšţãƒâ ™ on 5/5/2019.
 * ->
 */
class ChangePasswordDialog : BaseDialogFragment() {
    private lateinit var mUser: User
    override fun loadLayoutResource(): Int = R.layout.dialog_change_password

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHeight = .55

        mUser = arguments!!.getSerializable("user") as User

        btnOk.setOnClickListener { saveNewPassword() }
        btnCancel.setOnClickListener { dismiss() }
    }

    private fun validate(): Boolean {
        var cnt = 0

        when {
            etCurrentPassword.text.toString().trim().isEmpty() -> {
                etCurrentPassword.error = getString(R.string.toast_empty_field)
                cnt++
            }
            etCurrentPassword.text.toString().trim().length <= 4 -> {
                etCurrentPassword.error = getString(R.string.toast_password_less_5)
                cnt++
            }
            etCurrentPassword.text.toString().trim() != mUser.password -> {
                etCurrentPassword.error = getString(R.string.toast_password_error)
                cnt++
            }
            else -> etCurrentPassword.error = null
        }

        when {
            etNewPassword.text.toString().trim().isEmpty() -> {
                etNewPassword.error = getString(R.string.toast_empty_field)
                cnt++
            }
            etNewPassword.text.toString().trim().length <= 4 -> {
                etNewPassword.error = getString(R.string.toast_password_less_5)
                cnt++
            }
            etConfirmPassword.text.toString().trim() != etNewPassword.text.toString().trim() -> {
                etConfirmPassword.error = getString(R.string.toast_confirm_password)
                cnt++
            }
            else -> {
                etNewPassword.error = null
                etConfirmPassword.error = null
            }
        }
        return cnt == 0
    }

    private fun saveNewPassword() {
        if (validate()) {
            mUser.password = etNewPassword.text.toString().trim()
            Thread {
                mRoomDB.userDao().updateUser(mUser)
                activity!!.runOnUiThread { Logging.toast(context!!, R.string.toast_password_successfully) }
                dismiss()
            }.start()
        }
    }
}