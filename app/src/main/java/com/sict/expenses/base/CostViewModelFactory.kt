package com.sict.expenses.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sict.expenses.viewModel.CostViewModel

/**
 * Created by µðšţãƒâ ™ on 4/8/2019.
 * ->
 */
class CostViewModelFactory(private val application: Application, private val userId: Int, private val paymentId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CostViewModel(application, userId, paymentId) as T
}