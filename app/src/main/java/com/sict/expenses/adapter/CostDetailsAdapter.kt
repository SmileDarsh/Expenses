package com.sict.expenses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sict.expenses.R
import com.sict.expenses.helper.Utils
import com.sict.expenses.model.Cost
import kotlinx.android.synthetic.main.item_cost.view.*

/**
 * Created by µðšţãƒâ ™ on 4/21/2019.
 * ->
 */

class CostDetailsAdapter(private val mCosts: MutableList<Cost>) : RecyclerView.Adapter<CostHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostHolder =
        CostHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cost, parent, false))

    override fun getItemCount(): Int = mCosts.size

    override fun onBindViewHolder(holder: CostHolder, position: Int) = holder.bindView(mCosts[position])

    fun getAllCostPrice(): String {
        var price = 0.0
        mCosts.forEach {
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


