package com.shubinat.notforgot.domain.usecases.notes

import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.NoteRepository

class GetAllNotesUseCase(private val repository: NoteRepository) {

    operator fun invoke(user: User) {
        repository.getNotes(user)
    }
}