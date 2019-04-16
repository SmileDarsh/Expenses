package com.sict.expenses.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sict.expenses.R
import com.sict.expenses.adapter.ExpensesAdapter
import com.sict.expenses.base.BaseFragment
import com.sict.expenses.base.HomeViewModelFactory
import com.sict.expenses.model.Expenses
import com.sict.expenses.model.Wallet
import com.sict.expenses.popupDialog.AddWalletDialog
import com.sict.expenses.viewModel.ExpensesViewModel
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
class HomeFragment : BaseFragment() {
    private var mWallet = 0.0
    private val mAdapter = ExpensesAdapter()
    override fun loadLayoutResource(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mWallet = arguments!!.getDouble("wallet", 0.0)

        mAdapter.setWallet(mWallet)

        val vm = ViewModelProviders.of(
            activity!!, HomeViewModelFactory(activity!!.application, mUserId)
        ).get(ExpensesViewModel::class.java)

        vm.expensesList.observe(this, Observer {
            mAdapter.submitList(it)
        })
        initExpensesRecyclerView(view.rvExpenses)
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    /**
     * Come from [AddWalletDialog]
     * When user change Wallet from WalletDialog
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWalletChanged(wallet: Wallet) {
        mAdapter.setWallet(wallet.value)
        Thread {
            val expenses: Expenses? = mRoomDB.expensesDao().getLastExpenses(mUserId)
            if (expenses != null) {
                expenses.wallet = wallet.value
                mRoomDB.expensesDao().updateExpenses(expenses)
            }
        }.start()
    }

    private fun initExpensesRecyclerView(recycler: RecyclerView) {
        recycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            val mFab = activity!!.fab
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0)
                        mFab.hide()
                    else
                        mFab.show()
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }
}