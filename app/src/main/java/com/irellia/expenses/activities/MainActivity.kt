package com.irellia.expenses.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.irellia.expenses.R
import com.irellia.expenses.adapter.NavAdapter
import com.irellia.expenses.base.BaseActivity
import com.irellia.expenses.base.NavigationManager
import com.irellia.expenses.fragment.CostFragment
import com.irellia.expenses.fragment.HomeFragment
import com.irellia.expenses.helper.OpenDialog.openAddExpenses
import com.irellia.expenses.helper.OpenDialog.openChangePassword
import com.irellia.expenses.helper.OpenDialog.openChooseMonth
import com.irellia.expenses.helper.OpenDialog.openWallet
import com.irellia.expenses.helper.TypeReference
import com.irellia.expenses.helper.Utils.share
import com.irellia.expenses.model.NavMenu
import com.irellia.expenses.model.User
import com.irellia.expenses.popupDialog.AddPaymentDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {
    private lateinit var mUser: User

    override fun loadLayoutResource(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDrawerLayout()

        Thread {
            mUser = mRoomDB.userDao().getUser(intent.getIntExtra("userId", 0))
            navManager.open(
                addHomeFragment(), R.id.frame_layout, NavigationManager.OpenMethod.REPLACE, false, "HomeFragment"
            )
        }.start()

        fab.setOnClickListener {
            openAddExpenses(mUser.id!!, 0L).show(supportFragmentManager, "main")
        }

        navList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = NavAdapter(
                mutableListOf(
                    NavMenu(getString(R.string.home), R.drawable.ic_home, TypeReference.HOME),
                    NavMenu(getString(R.string.cost), R.drawable.ic_cost, TypeReference.COST),
                    NavMenu(getString(R.string.add_wallet), R.drawable.ic_add_box, TypeReference.WALLET),
                    NavMenu(getString(R.string.debtors), R.drawable.ic_payment, TypeReference.ADD_PAYMENT),
                    NavMenu(
                        getString(R.string.change_password),
                        R.drawable.ic_change_password,
                        TypeReference.CHANGE_PASSWORD
                    ),
                    NavMenu(
                        getString(R.string.share),
                        R.drawable.ic_share,
                        TypeReference.SHARE
                    ),
                    NavMenu(getString(R.string.logout), R.drawable.ic_logout, TypeReference.LOGOUT)
                )
            )
        }

        btnDate.setOnClickListener {
            openChooseMonth(mUser).show(supportFragmentManager, "monthPickerDialog")
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
                toolbar.setTitle(R.string.home)
                fab.show()
            }
            TypeReference.COST -> {
                navManager.open(
                    addCostFragment(), R.id.frame_layout, NavigationManager.OpenMethod.REPLACE, false, "main"
                )
                toolbar.setTitle(R.string.cost)
                fab.hide()
            }
            TypeReference.WALLET -> {
                openWallet(mUser.id!!).show(supportFragmentManager, "main")
            }
            TypeReference.ADD_PAYMENT -> {
                AddPaymentDialog().show(supportFragmentManager, "main")
            }
            TypeReference.CHANGE_PASSWORD -> {
                openChangePassword(mUser).show(supportFragmentManager, "main")
            }
            TypeReference.SHARE -> {
                share(this@MainActivity)
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
