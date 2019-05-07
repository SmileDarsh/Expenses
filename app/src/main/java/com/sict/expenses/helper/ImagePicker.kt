package com.sict.expenses.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.sict.expenses.R

/**
 * Created by µðšţãƒâ ™ on 5/7/2019.
 * ->
 */
class ImagePicker(private val mContext: Context, private val userImage: ImageView) {
    private val REQUEST_CODE = View.generateViewId()
    private val REQUEST_WRITE_PERMISSION = View.generateViewId()
    var mUserImage = ""

    private fun chooseImage() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        (mContext as Activity).startActivityForResult(galleryIntent, REQUEST_CODE)
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (mContext as Activity).requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_PERMISSION
            )
        }
    }

    @SuppressLint("Recycle")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK && data != null) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = mContext.contentResolver.query(
                data.data!!,
                filePathColumn, null, null, null
            )!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mUserImage = cursor.getString(columnIndex)
            Glide.with(mContext)
                .load(mUserImage)
                .into(userImage)
            cursor.close()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == REQUEST_WRITE_PERMISSION)
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                chooseImage()
            } else {
                Logging.toast(mContext, R.string.toast_accept_permission)
            }
    }
}