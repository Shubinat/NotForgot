package com.shubinat.notforgot.data.roomWithRetrofit.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shubinat.notforgot.data.roomWithRetrofit.entities.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category WHERE user_id = :userId")
    fun getAll(userId: Int) : List<Category>

    @Query("SELECT * FROM Category WHERE id = :categoryId LIMIT 1")
    fun getCategory(categoryId: Int) : Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    @Query("DELETE FROM Category")
    fun clear()
}