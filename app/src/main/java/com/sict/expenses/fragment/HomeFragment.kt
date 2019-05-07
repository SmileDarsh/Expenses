package com.sict.expenses.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sict.expenses.R
import com.sict.expenses.adapter.ExpensesAdapter
import com.sict.expenses.base.BaseFragment
import com.sict.expenses.base.HomeViewModelFactory
import com.sict.expenses.helper.OpenDialog.openWallet
import com.sict.expenses.model.Wallet
import com.sict.expenses.viewModel.ExpensesViewModel
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
class HomeFragment : BaseFragment() {
    private lateinit var mFab: FloatingActionButton
    private val mAdapter = ExpensesAdapter()
    override fun loadLayoutResource(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFab = activity!!.fab

        val vm = ViewModelProviders.of(
            activity!!, HomeViewModelFactory(activity!!.application, mUserId)
        ).get(ExpensesViewModel::class.java)

        vm.expensesList.observe(this, Observer {
            checkList(it.size)
            mAdapter.submitList(it)
        })
        initExpensesRecyclerView()
        btnAdd.setOnClickListener {
            openWallet(mUserId).show(childFragmentManager, "home")
        }
    }

    private fun initExpensesRecyclerView() {
        rvExpenses.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
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

    private fun checkList(size: Int) {
        Thread {
            val wallet: Wallet? = mRoomDB.walletDao().getWallet(mUserId)
            activity!!.runOnUiThread {
                when {
                    wallet == null -> {
                        btnAdd.visibility = View.VISIBLE
                        tvNoData.visibility = View.GONE
                        mFab.hide()
                    }
                    size == 0 -> {
                        tvNoData.visibility = View.VISIBLE
                        btnAdd.visibility = View.GONE
                        mFab.show()
                    }
                    else -> {
                        tvNoData.visibility = View.GONE
                        btnAdd.visibility = View.GONE
                        mFab.show()
                    }
                }
            }
        }.start()

    }
}