package com.irellia.expenses.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.irellia.expenses.model.Expenses
import com.irellia.expenses.model.ExpensesWallet

/**
 * Created by µðšţãƒâ ™ on 4/3/2019.
 * ->
 */
@Dao
interface ExpensesDao {
    @Query(
        """SELECT expenses.* , user.* , wallet.* FROM expenses
            INNER JOIN user ON fkUserId = userId
            INNER JOIN wallet ON userId = walletUserId
            WHERE fkUserId = :userId  AND expensesMonth = userMonth AND expensesYear = userYear
            AND userMonth = walletMonth AND userYear = walletYear
            ORDER BY expensesDate ASC"""
    )
    fun getAllExpenses(userId: Int): DataSource.Factory<Int, ExpensesWallet>

    @Query("SELECT * FROM expenses WHERE fkUserId = :userId AND isDate = :date")
    fun getExpensesId(userId: Int, date: String): Expenses

    @Query("SELECT expensesId FROM expenses WHERE fkUserId = :userId")
    fun getAllExpensesId(userId: Int): MutableList<Int>

    @Query("SELECT COUNT(*) FROM expenses WHERE fkUserId = :userId AND isDate = :date")
    fun isExist(userId: Int, date: String): Int

    @Insert
    fun insertExpenses(expenses: Expenses)

    @Update
    fun updateExpenses(expenses: Expenses)

    @Query("DELETE FROM expenses WHERE expenses.expensesId = :id")
    fun deleteExpensesByUser(id: Int)
}