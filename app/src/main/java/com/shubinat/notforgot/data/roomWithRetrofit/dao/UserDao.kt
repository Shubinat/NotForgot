package com.shubinat.notforgot.data.roomWithRetrofit.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shubinat.notforgot.data.roomWithRetrofit.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    fun findUser(id: Int): User?

    @Query("SELECT * FROM User WHERE login = :login AND password = :password LIMIT 1")
    fun findUser(login: String, password: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(vararg users: User)

    @Query("DELETE FROM User")
    fun clear()
}