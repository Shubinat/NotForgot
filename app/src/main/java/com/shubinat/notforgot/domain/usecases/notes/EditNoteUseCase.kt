package com.shubinat.notforgot.domain.usecases.notes

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.repository.NoteRepository

class EditNoteUseCase(private val repository: NoteRepository) {

    operator fun invoke(note: Note) {
        repository.editNote(note)
    }
}