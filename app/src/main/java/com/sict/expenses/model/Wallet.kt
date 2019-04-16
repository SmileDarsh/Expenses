package com.sict.expenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by µðšţãƒâ ™ on 4/14/2019.
 * ->
 */
@Entity(tableName = "wallet")
data class Wallet(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "walletId") var id: Int? = null,
    @ColumnInfo(name = "walletUserId")var userId: Int,
    @ColumnInfo(name = "walletMonth")var month: Int,
    @ColumnInfo(name = "walletYear")var year: Int,
    @ColumnInfo(name = "walletValue")var value: Double = 0.0
) : Serializable