package com.sict.expenses.model

import androidx.room.Embedded

/**
 * Created by µðšţãƒâ ™ on 4/23/2019.
 * ->
 */
data class ExpensesWallet(@Embedded val expenses: Expenses, @Embedded val user: User, @Embedded val wallet: Wallet)