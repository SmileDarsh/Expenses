package com.sict.expenses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sict.expenses.R
import com.sict.expenses.helper.Utils
import com.sict.expenses.model.Cost
import kotlinx.android.synthetic.main.item_cost.view.*

/**
 * Created by µðšţãƒâ ™ on 4/4/2019.
 * ->
 */
private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cost>() {
    override fun areItemsTheSame(
        @NonNull oldCost: Cost, @NonNull newCost: Cost
    ): Boolean {
        return oldCost.id == newCost.id
    }

    override fun areContentsTheSame(
        @NonNull oldCost: Cost, @NonNull newCost: Cost
    ): Boolean {
        return oldCost == newCost
    }
}

private const val COST_VIEW_TYPE = 0
private const val TOTAL_VIEW_TYPE = 1

class CostAdapter : PagedListAdapter<Cost, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COST_VIEW_TYPE ->
                CostHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cost, parent, false))
            else ->
                TotalHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_total, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CostHolder)
            holder.bindView(getItem(position)!!)
        else if (holder is TotalHolder)
            holder.bindView(getAllCostPrice())
    }

    override fun getItemViewType(position: Int): Int {
        val size = currentList!!.size - 1
        return if (size == position) TOTAL_VIEW_TYPE else COST_VIEW_TYPE
    }

    private fun getAllCostPrice(): String {
        var price = 0.0
        currentList!!.forEach {
            price += it.price
        }
        return "$price"
    }
}

class CostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(cost: Cost) {
        with(itemView) {
            tvKeyOne.text = Utils.dateFormat(cost.date)
            val price = "${cost.price} ${context.getString(R.string.egp)}"
            tvValueOne.text = price
        }
    }
}

class TotalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(costs: String) {
        with(itemView) {
            tvKeyOne.text = context.getString(R.string.total)
            val price = "$costs ${context.getString(R.string.egp)}"
            tvValueOne.text = price
        }
    }
}