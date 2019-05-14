package com.irellia.expenses.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.bumptech.glide.Glide
import com.irellia.expenses.R
import com.irellia.expenses.base.BaseActivity
import com.irellia.expenses.helper.ImagePicker
import com.irellia.expenses.model.User
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
class LoginActivity : BaseActivity() {
    private lateinit var mImagePicker: ImagePicker
    private lateinit var mUser: User

    override fun loadLayoutResource(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mUser = intent.getSerializableExtra("user") as User
        mImagePicker = ImagePicker(this, userImage)

        etPassword.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE)
                onLogin()
            return@setOnEditorActionListener false
        }

        Glide.with(this)
            .load(mUser.image)
            .into(userImage)
    }

    fun onLoginClicked(v: View) {
        onLogin()
    }

    private fun onLogin() {
        if (validate())
            goToMainActivity()
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
        if (mImagePicker.mUserImage.isNotEmpty())
            Thread {
                mUser.image = mImagePicker.mUserImage
                mRoomDB.userDao().updateUser(mUser)
            }.start()


        startActivity(
            Intent(this@LoginActivity, MainActivity::class.java)
                .putExtra("userId", mUser.id)
        )
        EventBus.getDefault().post("welcome")
        finish()
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