package com.sict.expenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
@Entity(tableName = "payment")
data class Payment(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "paymentId")
    var id: Int? = null,
    var name: String,
    @ColumnInfo(name = "paymentActive") var active: Boolean = true
)