package com.shubinat.notforgot.domain.usecases.categories

import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddCategoryUseCase(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category) {
        withContext(Dispatchers.IO) {
            repository.addCategory(category)
        }
    }
}