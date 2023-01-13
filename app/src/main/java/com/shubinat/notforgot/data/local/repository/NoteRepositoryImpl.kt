package com.shubinat.notforgot.data.local.repository

import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.NoteRepository

object NoteRepositoryImpl : NoteRepository {

    private val notes = mutableListOf<Note>()

    private var idCounter = 0


    override suspend fun addNote(note: Note) {
        if (note.title.isBlank()) throw RuntimeException("Title cannot be blank")
        if (note.description.isBlank()) throw RuntimeException("Description cannot be blank")

        if (note.id == 0) {
            note.id = ++idCounter
        } else {
            if (notes.firstOrNull { it.id == note.id } != null)
                throw RuntimeException("Note with id: ${note.id} is exist")
        }

        notes.add(note)

    }

    override suspend fun editNote(note: Note) {
        if (note.title.isBlank()) throw RuntimeException("Title cannot be blank")
        if (note.description.isBlank()) throw RuntimeException("Description cannot be blank")

        val existNote = notes.firstOrNull { it.id == note.id }
            ?: throw RuntimeException("Note not found")
        notes.remove(existNote)
        notes.add(note.copy(id = existNote.id))
    }

    override suspend fun getNotes(user: User): List<Note> {
        return notes.filter { it.user == user }.toList()
    }

    override suspend fun getNote(id: Int): Note {
        return notes.firstOrNull { it.id == id }
            ?: throw RuntimeException("Note not found")
    }
}