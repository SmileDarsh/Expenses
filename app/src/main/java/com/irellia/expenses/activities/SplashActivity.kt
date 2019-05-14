package com.irellia.expenses.activities

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
import com.irellia.expenses.R
import com.irellia.expenses.base.BaseActivity
import com.irellia.expenses.base.BaseFirebaseStorage
import com.irellia.expenses.helper.DbBackup.exportDatabaseFile
import com.irellia.expenses.helper.DbBackup.importDatabaseFile
import com.irellia.expenses.helper.Logging
import com.irellia.expenses.helper.Utils.getMacAddressAndModel
import com.irellia.expenses.helper.Utils.showAlertDialog

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
                    BaseFirebaseStorage(this@SplashActivity).addFile(getMacAddressAndModel())
                    intent.setClass(applicationContext, WelcomeActivity::class.java)
                } else {
                    importDatabaseFile(this@SplashActivity)
                    if (mRoomDB.userDao().getUsersCount() > 0)
                        intent.setClass(applicationContext, WelcomeActivity::class.java)
                    else {
                        intent.setClass(applicationContext, RegisterActivity::class.java)
                            .putExtra("firstTime", true)
                    }
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
                    showAlertDialog(this@SplashActivity, R.string.we_need_permission_storage,
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

        Logging.log("Mac : " + getMacAddressAndModel())
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