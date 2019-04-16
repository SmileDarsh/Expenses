package com.sict.expenses.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.sict.expenses.model.ExpensesCost

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
@Dao
interface ExpensesCostDao {
    @Query("SELECT expenses.* , cost.* FROM expenses INNER JOIN cost WHERE expenses.expensesId = cost.pExpensesId")
    fun getAllExpenses(): DataSource.Factory<Int, ExpensesCost>
}