package com.shubinat.notforgot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shubinat.notforgot.data.room.entity.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category WHERE user_id = :userId")
    abstract fun getAll(userId: Int) : List<Category>

    @Query("SELECT * FROM Category WHERE id = :categoryId LIMIT 1")
    abstract fun getCategory(categoryId: Int) : Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addCategory(category: Category)

}