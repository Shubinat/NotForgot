package com.shubinat.notforgot.domain.usecases.notes

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class GetNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Int): Note {
        return withContext(Dispatchers.IO) {
            repository.getNote(id)
        }
    }
}