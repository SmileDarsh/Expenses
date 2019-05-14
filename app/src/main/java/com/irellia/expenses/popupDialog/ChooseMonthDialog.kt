package com.irellia.expenses.popupDialog

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.irellia.expenses.R
import com.irellia.expenses.base.BaseDialogFragment
import com.irellia.expenses.model.User
import kotlinx.android.synthetic.main.dialog_month_picker.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by µðšţãƒâ ™ on 4/11/2019.
 * ->
 */
class ChooseMonthDialog : BaseDialogFragment() {
    private val mYears = mutableListOf<Int>()
    private lateinit var mUser: User

    override fun loadLayoutResource(): Int = R.layout.dialog_month_picker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHeight = .4
        initData()
        initMonthSpinner()
        initYearSpinner()
        btnAdd.setOnClickListener {
            Thread {
                mRoomDB.userDao().updateUser(mUser)
                EventBus.getDefault().post("change")
                dismiss()
            }.start()
        }
    }

    private fun initData() {
        mUser = arguments!!.getSerializable("user") as User

        for (it in 0..50)
            mYears.add(it + 2000)
    }

    private fun initMonthSpinner() {
        val monthAdapter =
            ArrayAdapter<String>(context!!, R.layout.spinner_item, resources.getStringArray(R.array.months))
        spMonth.adapter = monthAdapter
        spMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mUser.month = p2
            }
        }
        spMonth.setSelection(mUser.month)
    }

    private fun initYearSpinner() {
        val yearAdapter =
            ArrayAdapter<Int>(context!!, R.layout.spinner_item, mYears)
        spYear.adapter = yearAdapter
        spYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mUser.year = mYears[p2]
            }
        }
        spYear.setSelection(mYears.indexOf(mUser.year))
    }
}
