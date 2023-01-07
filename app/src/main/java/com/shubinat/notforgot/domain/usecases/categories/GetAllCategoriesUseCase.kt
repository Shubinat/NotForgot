package com.shubinat.notforgot.domain.usecases.categories

import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllCategoriesUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(user: User): List<Category> {
        return withContext(Dispatchers.IO) {
            repository.getCategories(user)
        }
    }
}