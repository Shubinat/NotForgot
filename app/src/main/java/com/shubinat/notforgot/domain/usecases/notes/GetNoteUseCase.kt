package com.shubinat.notforgot.domain.usecases.notes

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.repository.NoteRepository

class GetNoteUseCase(private val repository: NoteRepository) {
    operator fun invoke(id: Int) : Note {
        return repository.getNote(id)
    }
}