package com.irellia.expenses.popupDialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.irellia.expenses.R
import com.irellia.expenses.activities.SplashActivity
import com.irellia.expenses.base.BaseDialogFragment
import com.irellia.expenses.base.BaseFirebaseStorage
import com.irellia.expenses.helper.Logging
import com.irellia.expenses.helper.OnBackupDataRetrun
import com.irellia.expenses.helper.Utils
import com.irellia.expenses.helper.Utils.isNetworkAvailable
import kotlinx.android.synthetic.main.dialog_download_db_file.*

/**
 * Created by µðšţãƒâ ™ on 5/5/2019.
 * ->
 */
class DownloadDbFileDialog : BaseDialogFragment() {
    override fun loadLayoutResource(): Int = R.layout.dialog_download_db_file

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnOk.setOnClickListener { importData() }
        btnCancel.setOnClickListener { dismiss() }
    }

    private fun importData() {
        if (isNetworkAvailable(context!!)) {
            showProgress()
            BaseFirebaseStorage(context!!).importFile(Utils.getMacAddressAndModel(), object : OnBackupDataRetrun {
                override fun onSuccess() {
                    hideProgress()
                    startActivity(Intent(context!!, SplashActivity::class.java))
                    dismiss()
                    activity!!.finish()
                }

                override fun onFailure() {
                    hideProgress()
                    dismiss()
                }
            })
        } else
            Logging.toast(context!!, R.string.toast_internet)
    }

    private fun showProgress() {
        pbProgress.visibility = View.VISIBLE
        tvMassage.visibility = View.INVISIBLE
        btnOk.visibility = View.INVISIBLE
        btnCancel.visibility = View.INVISIBLE
    }

    private fun hideProgress() {
        pbProgress.visibility = View.GONE
        tvMassage.visibility = View.VISIBLE
        btnOk.visibility = View.VISIBLE
        btnCancel.visibility = View.VISIBLE
    }
}