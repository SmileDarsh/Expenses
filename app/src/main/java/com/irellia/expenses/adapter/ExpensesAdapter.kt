package com.irellia.expenses.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.irellia.expenses.R
import com.irellia.expenses.activities.ExpensesDetailsActivity
import com.irellia.expenses.helper.Utils.dateFormat
import com.irellia.expenses.helper.Utils.numberFormat
import com.irellia.expenses.model.ExpensesWallet
import kotlinx.android.synthetic.main.item_expenses.view.*

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExpensesWallet>() {
    override fun areItemsTheSame(
        @NonNull oldExpenses: ExpensesWallet, @NonNull newExpenses: ExpensesWallet
    ): Boolean {
        return oldExpenses.expenses.id == newExpenses.expenses.id
    }

    override fun areContentsTheSame(
        @NonNull oldExpenses: ExpensesWallet, @NonNull newExpenses: ExpensesWallet
    ): Boolean {
        return oldExpenses == newExpenses
    }
}

class ExpensesAdapter : PagedListAdapter<ExpensesWallet, ExpensesNewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesNewHolder =
        ExpensesNewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_expenses, parent, false))

    override fun onBindViewHolder(holder: ExpensesNewHolder, position: Int) = holder.bindView(getItem(position)!!)

    override fun submitList(pagedList: PagedList<ExpensesWallet>?) {
        if (pagedList != null && pagedList.size > 0) {
            var wa = pagedList[0]!!.wallet.value
            pagedList.forEach {
                wa -= it.expenses.price
                it.expenses.wallet = wa
            }
        }
        super.submitList(pagedList)
    }
}

class ExpensesNewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(expenses: ExpensesWallet) {
        with(itemView) {
            tvDate.text = dateFormat(expenses.expenses.date)
            tvExpenses.text = numberFormat(expenses.expenses.price)
            tvWallet.text = numberFormat(expenses.expenses.wallet)
            cvCard.setOnClickListener {
                context.startActivity(
                    Intent(context, ExpensesDetailsActivity::class.java)
                        .putExtra("expenses", expenses.expenses)
                )
            }
        }
    }
}