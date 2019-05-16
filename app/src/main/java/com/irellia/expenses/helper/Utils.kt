package com.irellia.expenses.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.text.format.DateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.irellia.expenses.R
import com.irellia.expenses.activities.SplashActivity
import java.net.NetworkInterface
import java.text.NumberFormat
import java.util.*
import kotlin.experimental.and
import android.net.ConnectivityManager
import android.content.Intent
import com.irellia.expenses.room.RoomDB


/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
object Utils {

    fun dateFormat(date: Long): CharSequence = DateFormat.format("EEE , dd-MMM-yyyy", date)

    fun dateFormat(context: Context, month: Int, year: Int): CharSequence =
        "${context.resources.getStringArray(R.array.months)[month]} , ${numberFormat(year)}"

    fun numberFormat(value: Any): String {
        val number = NumberFormat.getInstance(Locale.getDefault())
        number.isGroupingUsed = false
        return number.format(value)
    }

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

    fun getMacAddressAndModel(): String {
        try {
            val all = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue

                val macBytes = nif.hardwareAddress ?: return ""

                val res1 = StringBuilder()
                for (b in macBytes) {
                    val num = removeChart(Integer.toHexString((b and 0xFF.toByte()).toInt()))
                    res1.append("$num:")
                }

                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString() + ":" + Build.MODEL
            }
        } catch (ex: Exception) {
            //handle exception
            Logging.log("Mac Ex : ${ex.message}")
        }
        return ""
    }

    private fun removeChart(num: String): String {
        val number: String
        number = if (num.length > 2) {
            val size = num.length
            num.substring(size - 2)
        } else
            num
        return number
    }

    fun isNetworkAvailable(con: Context): Boolean {
        try {
            val cm = con
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo

            if (networkInfo != null && networkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun share(context : Context){
        //sharing implementation here
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(
            android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
        sharingIntent.putExtra(
            android.content.Intent.EXTRA_TEXT,
            String.format(
                context.getString(R.string.share_body),
                context.getString(R.string.share_link) + context.packageName
            )
        )
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }
}