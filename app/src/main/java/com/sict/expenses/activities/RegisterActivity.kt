package com.sict.expenses.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sict.expenses.R
import com.sict.expenses.base.BaseActivity
import com.sict.expenses.helper.Logging
import com.sict.expenses.model.User
import com.sict.expenses.model.Wallet
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
class RegisterActivity : BaseActivity() {
    private val REQUEST_CODE = View.generateViewId()
    private val REQUEST_WRITE_PERMISSION = View.generateViewId()
    private var mUserImage = ""
    override fun loadLayoutResource(): Int = R.layout.activity_register

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
                    val userId = mRoomDB.userDao().insertUser(
                        User(
                            name = etUserName.text.toString(),
                            password = etPassword.text.toString(),
                            image = mUserImage,
                            month = cal.get(Calendar.MONTH),
                            year = cal.get(Calendar.YEAR)
                        )
                    )
                    mRoomDB.walletDao().insertWallet(
                        Wallet(
                            userId = userId.toInt(),
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
        requestPermission()
    }

    private fun chooseImage() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, REQUEST_CODE)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_PERMISSION
            )
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK && data != null) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(
                data.data!!,
                filePathColumn, null, null, null
            )!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mUserImage = cursor.getString(columnIndex)
            Glide.with(this@RegisterActivity)
                .load(mUserImage)
                .into(userImage)
            cursor.close()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_WRITE_PERMISSION)
            if (ContextCompat.checkSelfPermission(this@RegisterActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                chooseImage()
            } else {
                Logging.toast(this@RegisterActivity, R.string.toast_accept_permission)
            }
    }
}