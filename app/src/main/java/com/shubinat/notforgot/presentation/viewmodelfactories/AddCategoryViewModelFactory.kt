package com.shubinat.notforgot.presentation.viewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.presentation.viewmodels.AddCategoryViewModel
import com.shubinat.notforgot.presentation.viewmodels.EditorViewModel

class AddCategoryViewModelFactory(private val authUser: User, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddCategoryViewModel::class.java)) {
            AddCategoryViewModel(authUser, application) as T
        } else {
            throw RuntimeException("Unknown modelClass: $modelClass")
        }
    }
}