package com.irellia.expenses.base

import android.content.Context
import android.os.Environment
import com.google.firebase.storage.FirebaseStorage
import com.irellia.expenses.R
import com.irellia.expenses.helper.DATABASE_NAME_WITH_FORMAT
import com.irellia.expenses.helper.DbBackup.importDatabaseFile
import com.irellia.expenses.helper.Logging
import com.irellia.expenses.helper.OnBackupDataRetrun
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by µðšţãƒâ ™ on 5/12/2019.
 * ->
 */
class BaseFirebaseStorage(val mContext: Context) {
    private val mStorage = FirebaseStorage.getInstance()
    private var mStorageRef = mStorage.reference
    private var mFileName = "backup_$DATABASE_NAME_WITH_FORMAT"
    private var mSuffix = mutableListOf<Boolean>()
    private var mFilePath =
        Environment.getExternalStorageDirectory().path + "/Expenses/" + mFileName

    fun addFile(fileRefName: String) {
        exportFile(fileRefName, mFileName, mFilePath)
        exportFile(fileRefName, "$mFileName-shm", "$mFilePath-shm")
        exportFile(fileRefName, "$mFileName-wal", "$mFilePath-wal")
    }

    fun importFile(fileRefName: String, onBackupReturn: OnBackupDataRetrun) {
        importFile(fileRefName, mFileName, "db", onBackupReturn)
        importFile(fileRefName, "$mFileName-shm", "-shm", onBackupReturn)
        importFile(fileRefName, "$mFileName-wal", "-wal", onBackupReturn)
    }

    fun deleteFile(fileRefName: String) {
        deleteFile(fileRefName, mFileName)
        deleteFile(fileRefName, "$mFileName-shm")
        deleteFile(fileRefName, "$mFileName-wal")
    }

    private fun exportFile(fileRefName: String, fileName: String, filePath: String) {
        val fileRef = mStorageRef.child("$fileRefName/$fileName")
        val file = FileInputStream(File(filePath))
        val uploadTask = fileRef.putStream(file)

        uploadTask.addOnSuccessListener {
            if (it.metadata != null)
                Logging.log("uploadTask : ${it.metadata!!.name}")
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    private fun importFile(fileRefName: String, fileName: String, suffix: String, onBackupReturn: OnBackupDataRetrun) {
        val fileRef = mStorageRef.child("$fileRefName/$fileName")
        val localFile = File.createTempFile("application", suffix)
        fileRef.getFile(localFile).addOnSuccessListener {
            copyDataFromOneToAnother(localFile, if (suffix == "db") mFilePath else mFilePath + suffix)
            mSuffix.add(true)
            ifDataReturn(mSuffix.size , onBackupReturn)
        }.addOnFailureListener {
            mSuffix.add(true)
            ifDataError(mSuffix.size, it.message, onBackupReturn)
        }
    }

    private fun deleteFile(fileRefName: String, fileName: String) {
        val fileRef = mStorageRef.child("$fileRefName/$fileName")
        fileRef.delete()
    }

    private fun copyDataFromOneToAnother(file: File, toPath: String) {
        val inStream = file.inputStream()
        val outStream = FileOutputStream(toPath)

        inStream.use { input ->
            outStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun ifDataReturn(size: Int, onBackupReturn: OnBackupDataRetrun) {
        if (size == 3) {
            importDatabaseFile(mContext)
            onBackupReturn.onSuccess()
        }
    }

    private fun ifDataError(size: Int, message: String?, onBackupReturn: OnBackupDataRetrun) {
        if (size == 3) {
            if (message != null)
                Logging.toast(mContext, R.string.backup_not_exist)
            onBackupReturn.onFailure()
        }
    }
}