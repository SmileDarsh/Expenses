package com.sict.expenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sict.expenses.model.Payment

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
@Dao
interface PaymentsDao {
    @Query("SELECT * FROM payment WHERE paymentActive = 1 ORDER BY name DESC ")
    fun getAllPayments(): MutableList<Payment>

    @Query(
        """SELECT payment.* , fkPayments.* FROM payment INNER JOIN fkPayments ON fkUser = :userId
        WHERE fkPayment = paymentId AND  paymentActive = 1 ORDER BY name DESC """
    )
    fun getAllPaymentsByUser(userId: Int): MutableList<Payment>

    @Query("SELECT COUNT(*) FROM payment WHERE name = :name")
    fun isPayment(name: String): Int

    @Query("SELECT * FROM payment WHERE name = :name")
    fun getPayment(name: String): Payment

    @Query("SELECT * FROM payment WHERE paymentId = :id")
    fun getPayment(id: Int): Payment

    @Query("SELECT name FROM payment")
    fun getPaymentNames(): MutableList<String>

    @Insert
    fun insertPayment(payment: Payment): Long

    @Update
    fun updatePayment(payment: Payment)
}