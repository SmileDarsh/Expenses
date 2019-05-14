package com.irellia.expenses.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.irellia.expenses.model.ExpensesWallet
import com.irellia.expenses.room.RoomDB

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
class ExpensesViewModel(application: Application , userId : Int) : AndroidViewModel(application) {
    var expensesList: LiveData<PagedList<ExpensesWallet>> =
        LivePagedListBuilder(RoomDB.getInstance(application)!!.expensesDao().getAllExpenses(userId), 32).build()

}