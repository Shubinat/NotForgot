package com.shubinat.notforgot.data.roomWithRetrofit.repository

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.NoteRepository

class NoteRepositoryImpl : NoteRepository {
    override suspend fun addNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun editNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(user: User): List<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun getNote(id: Int): Note {
        TODO("Not yet implemented")
    }
}