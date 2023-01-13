package com.shubinat.notforgot.data.roomWithRetrofit.repository

import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.CategoryRepository

class CategoryRepositoryImpl : CategoryRepository {
    override suspend fun addCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(user: User): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategory(id: Int): Category {
        TODO("Not yet implemented")
    }
}