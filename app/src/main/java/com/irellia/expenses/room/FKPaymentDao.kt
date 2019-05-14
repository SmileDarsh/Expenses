package com.irellia.expenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.irellia.expenses.model.FKPayment

/**
 * Created by µðšţãƒâ ™ on 4/17/2019.
 * ->
 */
@Dao
interface FKPaymentDao {

    @Query("SELECT COUNT(*) FROM fkPayments WHERE fkUser = :userId AND fkPayment = :paymentId")
    fun isNotExist(userId: Int, paymentId: Int): Int

    @Insert
    fun insertFKPayment(fkPayment: FKPayment)
}