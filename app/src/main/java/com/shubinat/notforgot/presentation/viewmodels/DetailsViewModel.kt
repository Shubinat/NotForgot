package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shubinat.notforgot.R
import com.shubinat.notforgot.data.roomWithRetrofit.repository.NoteRepositoryImpl
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.Priority
import com.shubinat.notforgot.domain.usecases.notes.GetNoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsViewModel(private val noteId: Int, private val app: Application) :
    AndroidViewModel(app) {

    private val repository = NoteRepositoryImpl.getInstance(app)
    private val getNoteUseCase = GetNoteUseCase(repository)

    val categoryName = ObservableField<String>()
    val priorityName = ObservableField<String>()
    val priorityBackground = ObservableField<Drawable>()

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note>
        get() = _note

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    init {
        load()
    }

    fun load() {
        val operation = viewModelScope.async(Dispatchers.IO) {
            getNoteUseCase(noteId)
        }
        viewModelScope.launch(Dispatchers.Main) {
            delay(100)
            if (operation.isActive) {
                try {
                    _loading.value = true
                    val currentNote = operation.await()
                    onLoad(currentNote)
                } finally {
                    _loading.value = false
                }
            } else {
                val currentNote = operation.await()
                onLoad(currentNote)
            }
        }
    }

    private fun onLoad(note: Note) {
        _note.value = note
        categoryName.set(
            note.category?.name ?: app.getString(R.string.null_category_name)
        )
        priorityName.set(note.priority.getStringValue(app))
        priorityBackground.set(app.getDrawable(getPriorityBackgroundResource(note)))

    }

    private fun getPriorityBackgroundResource(note: Note): Int {
        return when (note.priority) {
            Priority.EMPTY -> R.color.white
            Priority.LOW -> R.drawable.priority_low_background
            Priority.MIDDLE -> R.drawable.priority_middle_background
            Priority.IMPORTANT -> R.drawable.priority_important_background
            Priority.VERY_IMPORTANT -> R.drawable.priority_very_important_background
        }
    }
}