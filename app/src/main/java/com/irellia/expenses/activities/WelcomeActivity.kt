package com.irellia.expenses.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.irellia.expenses.R
import com.irellia.expenses.adapter.UserAdapter
import com.irellia.expenses.base.BaseActivity
import com.irellia.expenses.room.RoomDB
import kotlinx.android.synthetic.main.activity_welcome.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
class WelcomeActivity : BaseActivity() {
    override fun loadLayoutResource(): Int = R.layout.activity_welcome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            rvUsers.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = UserAdapter(RoomDB.getInstance(context)!!.userDao().getAllUser())
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
     * welcome Text -> Come from [LoginActivity.goToMainActivity]
     * list Text -> Come from [UserAdapter]
     *
     * 1- welcome is mean when user login account we need finish this activity because it's still opened
     * 2- list is mean when user delete account from list if this account the last account
     * then we need return this user to [RegisterActivity] to create new account
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFinishActivity(finish: String) {
        if (finish == "welcome")
            finish()
        else if (finish == "list") {
            goToRegisterActivity()
            finish()
        }
    }

    fun onCreateAccountClicked(v: View) {
        goToRegisterActivity()
    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java)
            .putExtra("firstTime" , false))
    }
}