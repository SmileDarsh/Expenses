package com.sict.expenses.adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sict.expenses.R
import com.sict.expenses.activities.LoginActivity
import com.sict.expenses.helper.Utils
import com.sict.expenses.model.User
import com.sict.expenses.room.RoomDB
import kotlinx.android.synthetic.main.item_user.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
class UserAdapter(private val mUsers: MutableList<User>) : RecyclerView.Adapter<UserHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder =
        UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

    override fun getItemCount(): Int = mUsers.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = mUsers[position]
        holder.bindView(user)
        holder.mBtnDelete.setOnClickListener {
            Utils.showAlertDialog(it.context, R.string.toast_user_deleted, DialogInterface.OnClickListener { _, _ ->
                Thread {
                    val roomDB = RoomDB.getInstance(it.context)!!
                    val allExpensesIds = roomDB.expensesDao().getAllExpensesId(user.id!!)
                    allExpensesIds.forEach {
                        roomDB.costDao().deleteCostByUser(it)
                        roomDB.expensesDao().deleteExpensesByUser(it)
                    }
                    roomDB.userDao().deleteUser(user)
                    notifyItemRemoved(position)
                    mUsers.remove(user)

                    if (mUsers.size == 0) {
                        EventBus.getDefault().post("list")
                    }
                }.start()
            })
        }
    }
}

class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mUserImage = itemView.userImage!!
    private val mUserName = itemView.tvUserName!!
    private val mLLCard = itemView.llUser!!
    val mBtnDelete = itemView.ibnDelete!!

    fun bindView(user: User) {
        Glide.with(itemView.context)
            .load(user.image)
            .into(mUserImage)

        mUserName.text = user.name
        mLLCard.setOnClickListener {
            itemView.context.startActivity(
                Intent(itemView.context, LoginActivity::class.java)
                    .putExtra("user", user)
            )
        }
    }
}