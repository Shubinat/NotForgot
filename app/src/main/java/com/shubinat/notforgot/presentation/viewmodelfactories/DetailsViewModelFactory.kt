package com.shubinat.notforgot.presentation.viewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.presentation.viewmodels.DetailsViewModel

class DetailsViewModelFactory(private val noteId: Int, private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            DetailsViewModel(noteId, application) as T
        } else{
            throw RuntimeException("Unknown modelClass: $modelClass")
        }

    }
}