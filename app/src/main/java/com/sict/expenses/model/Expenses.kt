package com.sict.expenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by µðšţãƒâ ™ on 4/3/2019.
 * ->
 */
@Entity(tableName = "expenses")
data class Expenses(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "expensesId") var id: Int? = null,
    @ColumnInfo(name = "fkUserId") var userId: Int,
    @ColumnInfo(name = "expensesDate") var date: Long,
    var isDate: String,
    var wallet: Double = 0.0,
    @ColumnInfo(name = "expensesMonth")var month : Int,
    @ColumnInfo(name = "expensesYear")var year : Int,
    @ColumnInfo(name = "expensesPrice") var price: Double,
    @ColumnInfo(name = "expensesActive") var active: Boolean = true
) : Serializable