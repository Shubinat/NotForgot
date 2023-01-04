package com.shubinat.notforgot.domain.usecases.notes

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AddNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        withContext(Dispatchers.IO) {
            repository.addNote(note)
        }
    }
}