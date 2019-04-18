package com.sict.expenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by µðšţãƒâ ™ on 4/17/2019.
 * ->
 */
@Entity(tableName = "fkPayments")
data class FKPayment(
    @PrimaryKey @ColumnInfo(name = "FKPaymentId") var id: Int? = null,
    @ColumnInfo(name = "fkUser") var userId: Int,
    @ColumnInfo(name = "fkPayment") var paymentId: Int
)