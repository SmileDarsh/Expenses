package com.sict.expenses.activities

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.sict.expenses.R
import com.sict.expenses.base.BaseActivity
import com.sict.expenses.helper.DbBackup.exportDatabaseFile
import com.sict.expenses.helper.DbBackup.importDatabaseFile
import com.sict.expenses.helper.Utils

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */

class SplashActivity : BaseActivity() {
    private val MY_PERMISSIONS_REQUEST_READ_STORAGE = 99
    private var mDelayHandler: Handler? = null
    private val mSplashDelay: Long = 3000
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            Thread {
                val intent = Intent()
                if (mRoomDB.userDao().getUsersCount() > 0) {
                    exportDatabaseFile(this@SplashActivity)
                    intent.setClass(applicationContext, WelcomeActivity::class.java)
                } else {
                    importDatabaseFile(this@SplashActivity)
                    if (mRoomDB.userDao().getUsersCount() > 0)
                        intent.setClass(applicationContext, WelcomeActivity::class.java)
                    else
                        intent.setClass(applicationContext, RegisterActivity::class.java)
                }
                startActivity(intent)
                finish()
            }.start()
        }
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            MY_PERMISSIONS_REQUEST_READ_STORAGE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mDelayHandler!!.postDelayed(mRunnable, mSplashDelay)
                } else {
                    Utils.showAlertDialog(this@SplashActivity, R.string.we_need_permission_storage,
                        DialogInterface.OnClickListener { _, _ ->
                            startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.parse("package:$packageName"))
                            )
                            finish()
                        })
                }
                return
            }
        }
    }

    override fun loadLayoutResource(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDelayHandler = Handler()

        requestPermissions(this)
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}