package com.shubinat.notforgot.presentation.viewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.presentation.viewmodels.EditorViewModel

class EditorViewModelFactory(
    private val application: Application,
    private val user : User,
    private val note: Note?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EditorViewModel::class.java)) {
            EditorViewModel(application, user, note) as T
        } else {
            throw RuntimeException("Unknown modelClass: $modelClass")
        }
    }
}