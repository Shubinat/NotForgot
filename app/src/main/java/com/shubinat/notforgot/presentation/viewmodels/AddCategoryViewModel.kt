package com.shubinat.notforgot.presentation.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.shubinat.notforgot.data.local.repository.CategoryRepositoryImpl
import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.usecases.categories.AddCategoryUseCase

class AddCategoryViewModel(private val authUser: User) : ViewModel() {
    val repository = CategoryRepositoryImpl
    val addCategoryUseCase = AddCategoryUseCase(repository)
    val name = ObservableField<String>()

    fun save() : Boolean {
        return try {
            addCategoryUseCase(Category(0, name.get() ?: "", authUser))
            true
        } catch (ex : Exception){
            false
        }
    }
}