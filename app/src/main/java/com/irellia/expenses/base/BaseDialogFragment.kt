package com.irellia.expenses.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.irellia.expenses.R
import com.irellia.expenses.helper.Utils
import com.irellia.expenses.room.RoomDB

/**
 * Created by µðšţãƒâ ™ on 4/14/2019.
 * ->
 */
abstract class BaseDialogFragment : DialogFragment() {
    lateinit var mRoomDB: RoomDB
    var mWidth = .80
    var mHeight = .30
    abstract fun loadLayoutResource(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(loadLayoutResource(), container, false)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawableResource(R.drawable.curved_corners_layout)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        mRoomDB = RoomDB.getInstance(context!!)!!

        return v
    }

    override fun onResume() {
        super.onResume()
        Utils.setWindowLayoutForDialog(dialog, mWidth, mHeight)
    }
}