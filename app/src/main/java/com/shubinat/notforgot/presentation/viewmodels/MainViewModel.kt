package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import android.app.LauncherActivity.ListItem
import android.opengl.Visibility
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.shubinat.notforgot.data.room.repository.NoteRepositoryImpl
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.usecases.notes.EditNoteUseCase
import com.shubinat.notforgot.domain.usecases.notes.GetAllNotesUseCase
import com.shubinat.notforgot.presentation.adapters.NotesAdapter
import com.shubinat.notforgot.presentation.listitems.CategoryItem
import com.shubinat.notforgot.presentation.listitems.NoteItem
import com.shubinat.notforgot.presentation.listitems.NoteListItem

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
        NoteRepositoryImpl(application)

    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)

    lateinit var notes: List<NoteListItem>

    val visibility: Int
        get() = if (notes.isNotEmpty()) View.GONE else View.VISIBLE


    fun getAllNotes(user: User) {
        val listItems = mutableListOf<NoteListItem>()

        val allNotes = getAllNotesUseCase(user)
        val groupingNotes = allNotes.groupBy { it.category }
        for (group in groupingNotes) {
            listItems.add(CategoryItem(group.key))
            for (note in group.value) {
                listItems.add(NoteItem(note))
            }
        }
        notes = listItems
    }

    fun changeCompletedStatus(note: Note) {
        editNoteUseCase(note.copy(completed = !note.completed))
    }

}
