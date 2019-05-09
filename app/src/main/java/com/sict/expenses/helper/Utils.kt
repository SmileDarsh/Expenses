package com.sict.expenses.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.format.DateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.sict.expenses.R
import com.sict.expenses.activities.SplashActivity
import java.text.NumberFormat
import java.util.*


/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
object Utils {

    fun dateFormat(date: Long): CharSequence = DateFormat.format("EEE , dd-MMM-yyyy", date)

    fun dateFormat(context: Context, month: Int, year: Int): CharSequence =
        "${context.resources.getStringArray(R.array.months)[month]} , ${replaceArabicNumbers(year.toString())}"

    fun numberFormat(value: Any): String = NumberFormat.getInstance(Locale.getDefault()).format(value)

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

//    fun getTime(): Long {
//        val calendar = Calendar.getInstance()
//        val day = Calendar.DAY_OF_MONTH
//        val month = Calendar.MONTH
//        val year = Calendar.YEAR
//
//        calendar.set(year, month, day)
//
//        return calendar.time!!.time
//    }

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
        if (context is SplashActivity)
            builder.setNegativeButton(R.string.cancel) { _, _ -> (context as Activity).finish() }
        else
            builder.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.setPositiveButton(R.string.ok, action)
        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("InflateParams")
    fun showPasswordDialog(context: Context, userName: String, action: OnNegativeButtonListener) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_password, null, false)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnOk = view.findViewById<TextView>(R.id.btnOk)
        val btnCancel = view.findViewById<TextView>(R.id.btnCancel)

        tvTitle.text = String.format(context.getString(R.string.delete_user), userName)
        btnOk.setOnClickListener {
            if (etPassword.text.toString().trim().isNotEmpty()) {
                etPassword.error = null
                action.onNegativeButtonClick(dialog, etPassword)
            } else
                etPassword.error = context.getString(R.string.toast_empty_field)
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.setContentView(view)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawableResource(R.drawable.curved_corners_layout)
        setWindowLayoutForDialog(dialog, .85, .40)
        dialog.show()
    }

    private fun replaceArabicNumbers(original: String): String {
        return if (Locale.getDefault().displayLanguage == "العربية")
            original.replace("1".toRegex(), "١")
                .replace("2".toRegex(), "٢")
                .replace("3".toRegex(), "٣")
                .replace("4".toRegex(), "٤")
                .replace("5".toRegex(), "٥")
                .replace("6".toRegex(), "٦")
                .replace("7".toRegex(), "٧")
                .replace("8".toRegex(), "٨")
                .replace("9".toRegex(), "٩")
                .replace("0".toRegex(), "٠")
        else
            original
    }
}