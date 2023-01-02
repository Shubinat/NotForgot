package com.shubinat.notforgot.data.local.repository

import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.CategoryRepository

object CategoryRepositoryImpl : CategoryRepository {

    private val categories = mutableListOf<Category>()

    private var idCounter = 0

    override fun addCategory(category: Category) {
        if (category.name.isBlank()) throw RuntimeException("Name cannot be blank")
        if (categories.firstOrNull { it.name == category.name } != null)
            throw RuntimeException("Category with name: ${category.name} is exist")

        if (category.id == 0) {
            category.id = ++idCounter
        } else {
            if (categories.firstOrNull { it.id == category.id } != null)
                throw RuntimeException("Category with id: ${category.id} is exist")
        }

        categories.add(category)
    }

    override fun getCategories(user: User): List<Category> {
        return categories.filter { it.user == user }.toList()
    }

    override fun getCategory(id: Int): Category {
        TODO("Not yet implemented")
    }
}