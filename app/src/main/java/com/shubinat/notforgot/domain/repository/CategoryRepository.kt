package com.shubinat.notforgot.domain.repository

import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.User

interface CategoryRepository {
    suspend fun addCategory(category: Category)
    suspend fun getCategories(user: User): List<Category>
    suspend fun getCategory(id: Int): Category
}