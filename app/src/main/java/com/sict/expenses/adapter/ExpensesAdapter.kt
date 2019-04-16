package com.sict.expenses.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sict.expenses.R
import com.sict.expenses.activities.ExpensesDetailsActivity
import com.sict.expenses.helper.Utils
import com.sict.expenses.model.Expenses
import kotlinx.android.synthetic.main.item_expenses.view.*

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Expenses>() {
    override fun areItemsTheSame(
        @NonNull oldExpenses: Expenses, @NonNull newExpenses: Expenses
    ): Boolean {
        return oldExpenses.id == newExpenses.id
    }

    override fun areContentsTheSame(
        @NonNull oldExpenses: Expenses, @NonNull newExpenses: Expenses
    ): Boolean {
        return oldExpenses == newExpenses
    }
}

class ExpensesAdapter : PagedListAdapter<Expenses, ExpensesHolder>(DIFF_CALLBACK) {

    private var mWallet = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesHolder =
        ExpensesHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_expenses, parent, false))

    override fun onBindViewHolder(holder: ExpensesHolder, position: Int) = holder.bindView(getItem(position)!!)

    fun setWallet(wallet: Double) {
        mWallet = wallet
    }

    override fun submitList(pagedList: PagedList<Expenses>?) {
        var wa = mWallet
        pagedList!!.forEach {
            wa -= it.price
            it.wallet = wa
        }
        super.submitList(pagedList)
    }
}

class ExpensesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(expenses: Expenses) {
        with(itemView) {
            tvDate.text = Utils.dateFormat(expenses.date)
            tvExpenses.text = expenses.price.toString()
            tvWallet.text = expenses.wallet.toString()
            cvCard.setOnClickListener {
                context.startActivity(
                    Intent(context, ExpensesDetailsActivity::class.java)
                        .putExtra("expenses", expenses)
                )
            }
        }
    }
}