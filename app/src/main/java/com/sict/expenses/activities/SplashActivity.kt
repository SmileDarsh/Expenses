package com.sict.expenses.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.sict.expenses.R
import com.sict.expenses.base.BaseActivity
import com.sict.expenses.room.RoomDB

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
class SplashActivity : BaseActivity() {
    private var mDelayHandler: Handler? = null
    private val mSplashDelay: Long = 3000
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            Thread {
                val intent = Intent()
                if (mRoomDB.userDao().getUsersCount() > 0)
                    intent.setClass(applicationContext, WelcomeActivity::class.java)
                else
                    intent.setClass(applicationContext, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }.start()
        }
    }

    override fun loadLayoutResource(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, mSplashDelay)
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}