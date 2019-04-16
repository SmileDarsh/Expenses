package com.sict.expenses.room

import androidx.room.*
import com.sict.expenses.model.User

/**
 * Created by µðšţãƒâ ™ on 4/7/2019.
 * ->
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUser(): MutableList<User>

    @Query("SELECT * FROM user WHERE userId = :id")
    fun getUser(id: Int): User

    @Query("SELECT COUNT(*) FROM user")
    fun getUsersCount(): Int

    @Query("SELECT COUNT(*) FROM user WHERE name = :name")
    fun isExist(name: String): Int

    @Update
    fun updateUser(user: User)

    @Insert
    fun insertUser(user: User) : Long

    @Delete
    fun deleteUser(user: User)
}