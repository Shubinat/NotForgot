package com.shubinat.notforgot.data.roomWithRetrofit.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shubinat.notforgot.data.roomWithRetrofit.entities.AccessToken

@Dao
interface AccessTokenDao {
    @Query("SELECT * FROM AccessToken WHERE user_id == :userId LIMIT 1")
    fun getAccessToken(userId : Int) : AccessToken?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateOrCreate(accessToken: AccessToken)
}