package com.shubinat.notforgot.domain.usecases.notes

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.repository.NoteRepository

class AddNoteUseCase(private val repository: NoteRepository) {
    operator fun invoke(note: Note) {
        repository.addNote(note)
    }
}