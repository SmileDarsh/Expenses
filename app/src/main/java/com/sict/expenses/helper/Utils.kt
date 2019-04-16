package com.sict.expenses.helper

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.format.DateFormat
import android.view.Gravity
import android.widget.DatePicker
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.sict.expenses.R
import java.util.*


/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
object Utils {

    fun dateFormat(date: Long): CharSequence = DateFormat.format("EEE , dd-MMM-yyyy", date)

    fun dateFormat(context: Context, month: Int, year: Int): CharSequence =
        "${context.resources.getStringArray(R.array.months)[month]} , $year"

    /**
     *
     * @param datePicker
     * @return a java.util.Date
     */
    fun getLongDateFromDatePicker(datePicker: DatePicker): Long {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time!!.time
    }

    fun getTime(): Long {
        val calendar = Calendar.getInstance()
        val day = Calendar.DAY_OF_MONTH
        val month = Calendar.MONTH
        val year = Calendar.YEAR

        calendar.set(year, month, day)

        return calendar.time!!.time
    }

    fun setWindowLayoutForDialog(dialog: Dialog, width: Double, height: Double) {
        val window = dialog.window
        if (window != null) {
            val display = dialog.context.resources.displayMetrics

            val displayWidth = display.widthPixels
            val displayHeight = display.heightPixels

            window.setLayout((displayWidth * width).toInt(), (displayHeight * height).toInt())
            window.setGravity(Gravity.CENTER)
        }
    }

    fun showAlertDialog(context: Context, @StringRes message: Int, action: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setNegativeButton(R.string.ok, action)
        builder.setPositiveButton(R.string.cancel) { _, _ -> }
        val dialog = builder.create()
        dialog.show()
    }
}