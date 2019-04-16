package com.sict.expenses.model

import androidx.room.Embedded

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
data class ExpensesCost(@Embedded val expenses: Expenses, @Embedded val cost: Cost)