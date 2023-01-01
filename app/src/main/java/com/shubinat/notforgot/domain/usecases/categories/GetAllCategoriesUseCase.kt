package com.shubinat.notforgot.domain.usecases.categories

import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.CategoryRepository

class GetAllCategoriesUseCase(private val repository: CategoryRepository) {
    operator fun invoke(user: User) : List<Category> {
        return repository.getCategories(user)
    }
}