package com.sict.expenses.helper

import android.os.Bundle
import com.sict.expenses.model.User
import com.sict.expenses.popupDialog.*

/**
 * Created by µðšţãƒâ ™ on 4/21/2019.
 * ->
 */
object OpenDialog {

    fun openCostDetails(userId: Int, paymentId: Int): CostDetailsDialog {
        val dialogFragment = CostDetailsDialog()
        val bundle = Bundle()
        bundle.putInt("userId", userId)
        bundle.putInt("paymentId", paymentId)
        dialogFragment.arguments = bundle
        return dialogFragment
    }

    fun openAddExpenses(userId: Int, date: Long): AddExpensesDialog {
        val addExpensesDialog = AddExpensesDialog()
        val bundle = Bundle()
        bundle.putInt("userId", userId)
        bundle.putLong("date", date)
        addExpensesDialog.arguments = bundle
        return addExpensesDialog
    }

    fun openWallet(userId: Int): AddWalletDialog {
        val addWalletDialog = AddWalletDialog()
        val bundle = Bundle()
        bundle.putSerializable("userId", userId)
        addWalletDialog.arguments = bundle
        return addWalletDialog
    }

    fun openChooseMonth(user: User): ChooseMonthDialog {
        val chooseMonthDialog = ChooseMonthDialog()
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        chooseMonthDialog.arguments = bundle
        return chooseMonthDialog
    }

    fun openChangePassword(user: User): ChangePasswordDialog {
        val changePasswordDialog = ChangePasswordDialog()
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        changePasswordDialog.arguments = bundle
        return changePasswordDialog
    }
}