package com.irellia.expenses.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.irellia.expenses.model.Cost
import com.irellia.expenses.room.RoomDB


/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
class CostViewModel(application: Application, userId: Int, paymentId: Int) : AndroidViewModel(application) {
    private var mApplication = application
    private var mUserId = userId
    var costList: LiveData<PagedList<Cost>>? = null

    init {
        costList = initPagedList(paymentId)
    }

    private fun initPagedList(paymentId: Int): LiveData<PagedList<Cost>>? {
        return LivePagedListBuilder(
            RoomDB.getInstance(mApplication)!!.costDao().getAllCost(mUserId, paymentId),
            32
        ).build()
    }

    fun replaceSubscription(lifecycleOwner: LifecycleOwner, paymentId: Int) {
        costList!!.removeObservers(lifecycleOwner)
        costList = initPagedList(paymentId)
    }
}