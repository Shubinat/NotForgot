package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shubinat.notforgot.data.roomWithRetrofit.repository.NoteRepositoryImpl
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.usecases.notes.EditNoteUseCase
import com.shubinat.notforgot.domain.usecases.notes.GetAllNotesUseCase
import com.shubinat.notforgot.presentation.listitems.CategoryItem
import com.shubinat.notforgot.presentation.listitems.NoteItem
import com.shubinat.notforgot.presentation.listitems.NoteListItem
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
        NoteRepositoryImpl.getInstance(application)

    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)


    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _notes: MutableLiveData<List<NoteListItem>> = MutableLiveData()
    val notes: LiveData<List<NoteListItem>>
        get() = _notes

    fun getAllNotes(user: User) {
        val operation = viewModelScope.async(Dispatchers.IO) {
            getAllNotesUseCase(user)
        }
        viewModelScope.launch(Dispatchers.Main) {
            delay(100)
            if (operation.isActive) {
                try {
                    _loading.value = true
                    val allNotes = operation.await()
                    onGetAllNotes(allNotes)
                } finally {
                    _loading.value = false
                }

            } else {
                val allNotes = operation.await()
                onGetAllNotes(allNotes)
            }
        }


    }

    private fun onGetAllNotes(allNotes: List<Note>) {
        val listItems = mutableListOf<NoteListItem>()
        val groupingNotes = allNotes.groupBy { it.category }
        for (group in groupingNotes) {
            listItems.add(CategoryItem(group.key))
            for (note in group.value) {
                listItems.add(NoteItem(note))
            }
        }
        _notes.value = listItems
    }

    fun changeCompletedStatus(note: Note) {
        viewModelScope.launch {
            editNoteUseCase(note.copy(completed = !note.completed))
        }
    }

}
