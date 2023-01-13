package com.shubinat.notforgot.data.room.repository

import android.app.Application
import com.shubinat.notforgot.data.local.repository.CategoryRepositoryImpl
import com.shubinat.notforgot.data.room.database.AppDatabase
import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.CategoryRepository

class CategoryRepositoryImpl(application: Application) : CategoryRepository {
    private val db = AppDatabase.getInstance(application.applicationContext)
    private val userRepository = UserRepositoryImpl(application)

    override suspend fun addCategory(category: Category) {
        if (category.name.isBlank()) throw RuntimeException("Name cannot be blank")
        if (getCategories(category.user).firstOrNull { it.name == category.name } != null)
            throw RuntimeException("Category with name: ${category.name} is exist")

        db.categoryDao().addCategory(
            com.shubinat.notforgot.data.room.entity.Category(
                category.id,
                category.name,
                category.user.id
            )
        )
    }

    override suspend fun getCategories(user: User): List<Category> {
        return db.categoryDao().getAll(user.id).map {
            Category(it.id, it.name, user)
        }
    }

    override suspend fun getCategory(id: Int): Category {
        val category =
            db.categoryDao().getCategory(id) ?: throw RuntimeException("Category not found")
        val user = userRepository.getUser(category.userId)
        return Category(category.id, category.name, user)
    }
}