package com.shubinat.notforgot.domain.usecases.categories

import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.repository.CategoryRepository

class AddCategoryUseCase(private val repository: CategoryRepository) {
    operator fun invoke(category: Category) {
        repository.addCategory(category)
    }
}