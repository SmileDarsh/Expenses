package com.sict.expenses.room

import androidx.paging.DataSource
import androidx.room.*
import com.sict.expenses.model.Cost

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
@Dao
interface CostDao {
    @Query(
        """SELECT cost.* , expenses.* , user.* , payment.* FROM  cost INNER JOIN expenses ON expensesId = pExpensesId INNER JOIN user
            ON costMonth = userMonth AND costYear = userYear INNER JOIN payment ON paymentId = :paymentId
            WHERE fkUserId = :userId AND costActive = 1 ORDER BY costDay ASC"""
    )
    fun getAllCost(userId: Int , paymentId: Int): DataSource.Factory<Int, Cost>

    @Query("SELECT * FROM cost")
    fun getAllCost(): MutableList<Cost>

    @Query("SELECT * FROM cost WHERE cost.pExpensesId = :expensesId AND costActive = 1")
    fun getCostByExpenses(expensesId: Int): MutableList<Cost>

    @Query("DELETE FROM cost WHERE cost.pExpensesId = :parentId")
    fun deleteCostByUser(parentId: Int)

    @Query("SELECT costId FROM cost WHERE cost.pExpensesId = :parentId AND fkPaymentId = :paymentId")
    fun nameIsExist(parentId: Int, paymentId: Int): Int

    @Query("SELECT * FROM cost WHERE cost.pExpensesId = :parentId AND fkPaymentId = :paymentId")
    fun getCost(parentId: Int, paymentId: Int): Cost

    @Insert
    fun insertCost(cost: Cost): Long

    @Update
    fun updateCost(cost: Cost)

    @Delete
    fun deleteCost(cost: Cost)
}