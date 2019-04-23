package com.sict.expenses.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sict.expenses.model.Expenses
import com.sict.expenses.model.ExpensesWallet
import com.sict.expenses.room.RoomDB

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
class ExpensesViewModel(application: Application , userId : Int) : AndroidViewModel(application) {
    var expensesList: LiveData<PagedList<ExpensesWallet>> =
        LivePagedListBuilder(RoomDB.getInstance(application)!!.expensesDao().getAllExpensesNew(userId), 32).build()

}