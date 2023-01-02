package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.shubinat.notforgot.data.room.repository.CategoryRepositoryImpl
import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.usecases.categories.AddCategoryUseCase

class AddCategoryViewModel(private val authUser: User, application: Application) :
    AndroidViewModel(application) {

    val repository = CategoryRepositoryImpl(application)
    val addCategoryUseCase = AddCategoryUseCase(repository)
    val name = ObservableField<String>()

    fun save(): Boolean {
        return try {
            addCategoryUseCase(Category(0, name.get() ?: "", authUser))
            true
        } catch (ex: Exception) {
            false
        }
    }
}