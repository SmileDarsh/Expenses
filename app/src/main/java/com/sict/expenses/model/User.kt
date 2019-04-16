package com.sict.expenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "userId") var id: Int? = null,
    var name: String = "",
    var password: String = "",
    var image: String = "",
    @ColumnInfo(name = "userMonth") var month: Int,
    @ColumnInfo(name = "userYear") var year: Int,
    @ColumnInfo(name = "userActive") var active: Boolean = true
) : Serializable