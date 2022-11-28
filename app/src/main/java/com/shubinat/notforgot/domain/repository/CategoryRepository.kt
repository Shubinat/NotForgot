package com.shubinat.notforgot.domain.repository

import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.User

interface CategoryRepository {
    fun addCategory(category: Category)
    fun getCategories(user: User) : List<Category>
}