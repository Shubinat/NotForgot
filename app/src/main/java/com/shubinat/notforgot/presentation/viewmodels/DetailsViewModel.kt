package com.shubinat.notforgot.presentation.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableParcelable
import androidx.lifecycle.AndroidViewModel
import com.shubinat.notforgot.R
import com.shubinat.notforgot.data.local.repository.NoteRepositoryImpl
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.Priority
import com.shubinat.notforgot.domain.usecases.notes.GetNoteUseCase

class DetailsViewModel(private val noteId: Int, private val app: Application) :
    AndroidViewModel(app) {

    private val repository = NoteRepositoryImpl
    private val getNoteUseCase = GetNoteUseCase(repository)

    val note : ObservableParcelable<Note> = ObservableParcelable<Note>()
    val categoryName = ObservableField<String>()
    val priorityName = ObservableField<String>()
    val priorityBackground = ObservableField<Drawable>()

    init {
        load()
    }

    private fun getPriorityBackground(note: Note): Int {
        return when (note.priority) {
            Priority.EMPTY -> R.color.white
            Priority.LOW -> R.drawable.priority_low_background
            Priority.MIDDLE -> R.drawable.priority_middle_background
            Priority.IMPORTANT -> R.drawable.priority_important_background
            Priority.VERY_IMPORTANT -> R.drawable.priority_very_important_background
        }
    }

    fun load() {
        val currentNote = getNoteUseCase(noteId)
        note.set(currentNote)
        categoryName.set(currentNote.category?.name ?: app.getString(R.string.null_category_name))
        priorityName.set(currentNote.priority.getStringValue(app))
        priorityBackground.set(app.getDrawable(getPriorityBackground(currentNote)))
    }
}