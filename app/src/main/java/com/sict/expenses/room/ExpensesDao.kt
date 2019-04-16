package com.sict.expenses.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sict.expenses.model.Expenses

/**
 * Created by µðšţãƒâ ™ on 4/3/2019.
 * ->
 */
@Dao
interface ExpensesDao {
    @Query(
        """SELECT expenses.* , user.* FROM expenses INNER JOIN user ON expensesMonth = userMonth AND
            expensesYear = userYear WHERE fkUserId = :userId ORDER BY expensesDate ASC""")
    fun getAllExpenses(userId : Int): DataSource.Factory<Int, Expenses>

    @Query("SELECT * FROM expenses")
    fun getAllExpensess(): MutableList<Expenses>

    @Query("SELECT * FROM expenses WHERE fkUserId = :userId ORDER BY expensesDate ASC")
    fun getLastExpenses(userId: Int): Expenses

    @Query("SELECT * FROM expenses")
    fun getExpenses(): Expenses

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