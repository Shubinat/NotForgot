package com.shubinat.notforgot.domain.repository

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.User

interface NoteRepository {
    fun addNote(note: Note)
    fun editNote(note: Note)
    fun getNotes(user: User) : List<Note>
}