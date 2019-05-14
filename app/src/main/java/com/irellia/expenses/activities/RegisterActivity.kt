package com.irellia.expenses.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.irellia.expenses.R
import com.irellia.expenses.base.BaseActivity
import com.irellia.expenses.helper.ImagePicker
import com.irellia.expenses.helper.Logging
import com.irellia.expenses.model.User
import com.irellia.expenses.popupDialog.DownloadDbFileDialog
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
class RegisterActivity : BaseActivity() {
    private lateinit var mImagePicker: ImagePicker
    override fun loadLayoutResource(): Int = R.layout.activity_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImagePicker = ImagePicker(this, userImage)

        if(intent.extras!!["firstTime"] == true){
            DownloadDbFileDialog().show(supportFragmentManager , "register")
        }
    }

    private fun validate(): Boolean {
        var cnt = 0

        if (etUserName.text.toString().trim().isEmpty()) {
            etUserName.error = getString(R.string.toast_empty_field)
            cnt++
        } else {
            etUserName.error = null
        }

        when {
            etPassword.text.toString().trim().isEmpty() -> {
                etPassword.error = getString(R.string.toast_empty_field)
                cnt++
            }
            etPassword.text.toString().trim().length <= 4 -> {
                etPassword.error = getString(R.string.toast_password_less_5)
                cnt++
            }
            else -> etPassword.error = null
        }
        return cnt == 0
    }

    fun onRegisterClicked(v: View) {
        if (validate()) {
            val cal = Calendar.getInstance()
            Thread {
                if (mRoomDB.userDao().isExist(etUserName.text.toString().trim()) == 0) {
                    mRoomDB.userDao().insertUser(
                        User(
                            name = etUserName.text.toString(),
                            password = etPassword.text.toString(),
                            image = mImagePicker.mUserImage,
                            month = cal.get(Calendar.MONTH),
                            year = cal.get(Calendar.YEAR)
                        )
                    )
                    startActivity(Intent(this@RegisterActivity, WelcomeActivity::class.java))
                    finish()
                } else {
                    Snackbar.make(v, R.string.toast_user_exist, Snackbar.LENGTH_SHORT).show()
                }
            }.start()
        }
    }

    fun onImageProfileClicked(v: View) {
        mImagePicker.requestPermission()
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mImagePicker.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mImagePicker.onRequestPermissionsResult(requestCode)
    }
}