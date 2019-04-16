package com.sict.expenses.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sict.expenses.viewModel.ExpensesViewModel

/**
 * Created by µðšţãƒâ ™ on 4/8/2019.
 * ->
 */
class HomeViewModelFactory(private val application: Application, private val userId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ExpensesViewModel(application, userId) as T
}