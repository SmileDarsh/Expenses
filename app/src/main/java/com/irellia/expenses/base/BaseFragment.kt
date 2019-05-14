package com.irellia.expenses.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.irellia.expenses.room.RoomDB

abstract class BaseFragment : Fragment() {
    lateinit var mRoomDB: RoomDB
    var mUserId = 0

    abstract fun loadLayoutResource(): Int
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(loadLayoutResource(), container, false)
        mRoomDB = RoomDB.getInstance(context!!)!!
        mUserId = arguments!!.getInt("userId", 0)
        return v
    }

    fun showProgress() {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage("please wait...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun hideProgress() {
        if (progressDialog != null && progressDialog!!.isShowing())
            progressDialog!!.dismiss()
    }


}