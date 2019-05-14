package com.irellia.expenses.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.irellia.expenses.helper.DATABASE_NAME_WITH_FORMAT
import com.irellia.expenses.model.*

/**
 * Created by µðšţãƒâ ™ on 4/3/2019.
 * ->
 */


@Database(
    entities = [User::class, Expenses::class, Cost::class, Payment::class, Wallet::class, FKPayment::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun expensesDao(): ExpensesDao
    abstract fun costDao(): CostDao
    abstract fun paymentsDao(): PaymentsDao
    abstract fun walletDao(): WalletDao
    abstract fun fkPaymentDao(): FKPaymentDao

    companion object {
        private var INSTANCE: RoomDB? = null

        fun getInstance(context: Context): RoomDB? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room
                        .databaseBuilder(
                            context.applicationContext,
                            RoomDB::class.java,
                            DATABASE_NAME_WITH_FORMAT
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}