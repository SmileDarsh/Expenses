package com.irellia.expenses.base

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.irellia.expenses.R
import com.irellia.expenses.room.RoomDB
import org.greenrobot.eventbus.EventBus

abstract  class  BaseActivity : AppCompatActivity(){

    abstract fun loadLayoutResource():Int
    lateinit var navManager:NavigationManager
    private var progressDialog: ProgressDialog? = null
    private var bus:EventBus = EventBus.getDefault()
    lateinit var mRoomDB: RoomDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navManager = NavigationManager()
        navManager.init(supportFragmentManager, R.id.frame_layout)
        setContentView(loadLayoutResource())
        mRoomDB = RoomDB.getInstance(applicationContext)!!
    }

    fun showProgress() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("please wait...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun hideProgress() {
        if (progressDialog != null && progressDialog!!.isShowing())
            progressDialog!!.dismiss()
    }
}