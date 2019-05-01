package com.sict.expenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sict.expenses.model.Wallet

/**
 * Created by µðšţãƒâ ™ on 4/14/2019.
 * ->
 */
@Dao
interface WalletDao {
    @Query(
        """SELECT wallet.* , user.* FROM wallet INNER JOIN user ON walletUserId = userId
           WHERE walletUserId = :userId AND walletMonth = userMonth AND walletYear = userYear"""
    )
    fun getWallet(userId: Int): Wallet

    @Query("SELECT  * FROM wallet WHERE walletId = :id")
    fun getWalletById(id: Int): Wallet

    @Query(
        "SELECT *  FROM wallet"
    )
    fun getAllWallet(): MutableList<Wallet>

    @Insert
    fun insertWallet(wallet: Wallet): Long

    @Update
    fun updateWallet(wallet: Wallet)
}