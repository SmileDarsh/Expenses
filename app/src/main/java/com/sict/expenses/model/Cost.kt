package com.sict.expenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
@Entity(tableName = "cost")
data class Cost(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "costId") var id: Int? = null,
    @ColumnInfo(name = "fkCostUserId") var userId: Int = 0,
    @ColumnInfo(name = "fkPaymentId") var paymentId: Int = 0,
    var pExpensesId: Int = 0,
    var name: String = "",
    @ColumnInfo(name = "costDay") var day: Int = 0,
    @ColumnInfo(name = "costMonth") var month: Int = 0,
    @ColumnInfo(name = "costYear") var year: Int = 0,
    @ColumnInfo(name = "costDate") var date: Long = 0L,
    @ColumnInfo(name = "costPrice") var price: Double = 0.0,
    @ColumnInfo(name = "costActive") var active: Boolean = true
) : Serializable