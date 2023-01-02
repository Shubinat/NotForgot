package com.shubinat.notforgot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shubinat.notforgot.data.room.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    fun findUser(id: Int) : User?

    @Query("SELECT * FROM User WHERE login = :login AND password = :password LIMIT 1")
    fun findUser(login: String, password: String) : User?

    @Insert
    fun addUser(vararg users: User)
}