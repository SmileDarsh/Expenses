package com.sict.expenses.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.bumptech.glide.Glide
import com.sict.expenses.R
import com.sict.expenses.base.BaseActivity
import com.sict.expenses.model.User
import com.sict.expenses.model.Wallet
import com.sict.expenses.popupDialog.AddWalletDialog
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
class LoginActivity : BaseActivity() {
    private lateinit var mUser: User
    private var mWallet: Wallet? = null

    override fun loadLayoutResource(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mUser = intent.getSerializableExtra("user") as User

        Thread {
            mWallet = mRoomDB.walletDao().getWallet(mUser.id!!)
        }.start()

        etPassword.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE)
                onLogin()
            return@setOnEditorActionListener false
        }

        Glide.with(this)
            .load(mUser.image)
            .into(userImage)
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
     * Come from [AddWalletDialog.addWallet]
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFinishActivity(finish: String) {
        if (finish == "finish")
            goToMainActivity()
    }

    fun onLoginClicked(v: View) {
        onLogin()
    }

    private fun onLogin() {
        if (validate()) {
            if (mWallet!!.value == 0.0) {
                val addWalletDialog = AddWalletDialog()
                val bundle = Bundle()
                bundle.putSerializable("userId", mUser.id)
                addWalletDialog.arguments = bundle
                addWalletDialog.show(supportFragmentManager, "login")
            } else
                goToMainActivity()
        }
    }

    private fun validate(): Boolean {
        var cnt = 0
        when {
            etPassword.text.toString().trim().isEmpty() -> {
                etPassword.error = getString(R.string.toast_empty_field)
                cnt++
            }
            mUser.password != etPassword.text.toString() -> {
                etPassword.error = getString(R.string.toast_password_error)
                cnt++
            }
            else -> etPassword.error = null
        }

        return cnt == 0
    }

    private fun goToMainActivity() {
        startActivity(
            Intent(this@LoginActivity, MainActivity::class.java)
                .putExtra("userId", mUser.id)
        )
        EventBus.getDefault().post("welcome")
        finish()
    }
}