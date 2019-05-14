package com.irellia.expenses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.irellia.expenses.R
import com.irellia.expenses.model.NavMenu
import kotlinx.android.synthetic.main.item_nav.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
class NavAdapter(private val mUsers: MutableList<NavMenu>) : RecyclerView.Adapter<NavHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavHolder =
        NavHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_nav, parent, false))

    override fun getItemCount(): Int = mUsers.size

    override fun onBindViewHolder(holder: NavHolder, position: Int) {
        val user = mUsers[position]
        holder.bindView(user)
    }
}

class NavHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(nav: NavMenu) {
        itemView.tvNavName.text = nav.name
        itemView.ivNavIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, nav.icon))
        itemView.navItem.setOnClickListener {
            EventBus.getDefault().post(nav.type)
        }
    }
}