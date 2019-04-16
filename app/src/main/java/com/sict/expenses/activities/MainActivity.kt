package com.sict.expenses.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sict.expenses.R
import com.sict.expenses.adapter.NavAdapter
import com.sict.expenses.base.BaseActivity
import com.sict.expenses.base.NavigationManager
import com.sict.expenses.fragment.CostFragment
import com.sict.expenses.fragment.HomeFragment
import com.sict.expenses.helper.TypeReference
import com.sict.expenses.model.NavMenu
import com.sict.expenses.model.User
import com.sict.expenses.model.Wallet
import com.sict.expenses.popupDialog.AddExpensesDialog
import com.sict.expenses.popupDialog.AddPaymentDialog
import com.sict.expenses.popupDialog.AddWalletDialog
import com.sict.expenses.popupDialog.ChooseMonthDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {
    private lateinit var mUser: User
    private lateinit var mWallet: Wallet

    override fun loadLayoutResource(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDrawerLayout()

        Thread {
            mUser = mRoomDB.userDao().getUser(intent.getIntExtra("userId", 0))
            mWallet = mRoomDB.walletDao().getWallet(mUser.id!!)
            navManager.open(
                addHomeFragment(), R.id.frame_layout, NavigationManager.OpenMethod.REPLACE, false, "HomeFragment"
            )
        }.start()

        fab.setOnClickListener {
            val addExpensesDialog = AddExpensesDialog()
            val bundle = Bundle()
            bundle.putInt("userId", mUser.id!!)
            bundle.putLong("date", 0L)
            addExpensesDialog.arguments = bundle
            addExpensesDialog.show(supportFragmentManager, "addExpenses")
        }

        navList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = NavAdapter(
                mutableListOf(
                    NavMenu(getString(R.string.home), R.drawable.ic_home, TypeReference.HOME),
                    NavMenu(getString(R.string.cost), R.drawable.ic_cost, TypeReference.COST),
                    NavMenu(getString(R.string.add_wallet), R.drawable.ic_add_box, TypeReference.WALLET),
                    NavMenu(getString(R.string.add_payment), R.drawable.ic_payment, TypeReference.ADD_PAYMENT),
                    NavMenu(getString(R.string.logout), R.drawable.ic_logout, TypeReference.LOGOUT)
                )
            )
        }

        btnDate.setOnClickListener {
            val chooseMonthDialog = ChooseMonthDialog()
            val bundle = Bundle()
            bundle.putSerializable("user", mUser)
            chooseMonthDialog.arguments = bundle
            chooseMonthDialog.show(supportFragmentManager, "monthPickerDialog")
        }
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
     * Come from [NavAdapter]
     * When user click on item from sideMenu
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNavMenuAction(type: TypeReference) {
        when (type) {
            TypeReference.HOME -> {
                navManager.open(
                    addHomeFragment(), R.id.frame_layout, NavigationManager.OpenMethod.REPLACE, false, "main"
                )
                fab.show()
            }
            TypeReference.COST -> {
                navManager.open(
                    addCostFragment(), R.id.frame_layout, NavigationManager.OpenMethod.REPLACE, false, "main"
                )
                fab.hide()
            }
            TypeReference.WALLET -> {
                val addWalletDialog = AddWalletDialog()
                val bundle = Bundle()
                bundle.putSerializable("userId", mUser.id)
                addWalletDialog.arguments = bundle
                addWalletDialog.show(supportFragmentManager, "main")
            }
            TypeReference.ADD_PAYMENT -> {
                AddPaymentDialog().show(supportFragmentManager, "main")
            }
            TypeReference.LOGOUT -> {
                startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(drawerPane)
    }

    private fun addHomeFragment(): HomeFragment {
        val homeFragment = HomeFragment()
        val bundle = Bundle()
        bundle.putInt("userId", mUser.id!!)
        bundle.putDouble("wallet", mWallet.value)
        homeFragment.arguments = bundle
        return homeFragment
    }

    private fun addCostFragment(): CostFragment {
        val costFragment = CostFragment()
        val bundle = Bundle()
        bundle.putInt("userId", mUser.id!!)
        costFragment.arguments = bundle
        return costFragment
    }

    private fun setDrawerLayout() {
        // Create listener for drawer layout
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout, R.string.drawer_opened,
            R.string.drawer_closed
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }
        }
        btnMenu.setOnClickListener { drawerLayout.openDrawer(drawerPane) }
        drawerLayout.closeDrawer(drawerPane)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
    }
}
