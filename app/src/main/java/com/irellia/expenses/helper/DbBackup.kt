package com.irellia.expenses.helper

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

/**
 * Created by µðšţãƒâ ™ on 5/6/2019.
 * ->
 */
const val DATABASE_NAME_WITH_FORMAT = "expensesOne.db"

object DbBackup {

    fun exportDatabaseFile(context: Context) {
        try {
            copyDataFromOneToAnother(
                context.getDatabasePath(DATABASE_NAME_WITH_FORMAT).path,
                Environment.getExternalStorageDirectory().path + "/Expenses/backup_" + DATABASE_NAME_WITH_FORMAT
            )
            copyDataFromOneToAnother(
                context.getDatabasePath("$DATABASE_NAME_WITH_FORMAT-shm").path,
                Environment.getExternalStorageDirectory().path + "/Expenses/backup_" + DATABASE_NAME_WITH_FORMAT + "-shm"
            )
            copyDataFromOneToAnother(
                context.getDatabasePath("$DATABASE_NAME_WITH_FORMAT-wal").path,
                Environment.getExternalStorageDirectory().path + "/Expenses/backup_" + DATABASE_NAME_WITH_FORMAT + "-wal"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun importDatabaseFile(context: Context) {
        try {
            copyDataFromOneToAnother(
                Environment.getExternalStorageDirectory().path + "/Expenses/backup_" + DATABASE_NAME_WITH_FORMAT,
                context.getDatabasePath(DATABASE_NAME_WITH_FORMAT).path
            )
            copyDataFromOneToAnother(
                Environment.getExternalStorageDirectory().path + "/Expenses/backup_" + DATABASE_NAME_WITH_FORMAT + "-shm",
                context.getDatabasePath("$DATABASE_NAME_WITH_FORMAT-shm").path
            )
            copyDataFromOneToAnother(
                Environment.getExternalStorageDirectory().path + "/Expenses/backup_" + DATABASE_NAME_WITH_FORMAT + "-wal",
                context.getDatabasePath("$DATABASE_NAME_WITH_FORMAT-wal").path
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun copyDataFromOneToAnother(fromPath: String, toPath: String) {
        val inStream = File(fromPath).inputStream()
        val outStream = FileOutputStream(toPath)

        inStream.use { input ->
            outStream.use { output ->
                input.copyTo(output)
            }
        }
    }
}