package com.shubinat.notforgot.domain.repository

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.User

interface NoteRepository {
    suspend fun addNote(note: Note)
    suspend fun editNote(note: Note)
    suspend fun getNotes(user: User) : List<Note>
    suspend fun getNote(id: Int) : Note
}