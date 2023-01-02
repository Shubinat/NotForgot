package com.shubinat.notforgot.data.room.repository

import android.app.Application
import com.shubinat.notforgot.data.local.repository.NoteRepositoryImpl
import com.shubinat.notforgot.data.room.database.AppDatabase
import com.shubinat.notforgot.domain.entity.Category
import com.shubinat.notforgot.domain.entity.Note
import com.shubinat.notforgot.domain.entity.Priority
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.NoteRepository
import java.time.LocalDate

class NoteRepositoryImpl(application: Application) : NoteRepository {
    private val db = AppDatabase.getInstance(application.applicationContext)
    private val categoryRepository = CategoryRepositoryImpl(application)
    private val userRepository = UserRepositoryImpl(application)

    override fun addNote(note: Note) {
        if (note.title.isBlank()) throw RuntimeException("Title cannot be blank")
        if (note.description.isBlank()) throw RuntimeException("Description cannot be blank")

        db.noteDao().addNote(
            com.shubinat.notforgot.data.room.entity.Note(
                note.id,
                note.title,
                note.description,
                note.creationDate.toString(),
                note.completionDate.toString(),
                note.completed,
                note.category?.id,
                Priority.values().indexOf(note.priority),
                note.user.id
            )
        )
    }

    override fun editNote(note: Note) {
        if (note.title.isBlank()) throw RuntimeException("Title cannot be blank")
        if (note.description.isBlank()) throw RuntimeException("Description cannot be blank")

        getNotes(note.user).firstOrNull { it.id == note.id }
            ?: throw RuntimeException("Note not found")

        db.noteDao().editNote(
            com.shubinat.notforgot.data.room.entity.Note(
                note.id,
                note.title,
                note.description,
                note.creationDate.toString(),
                note.completionDate.toString(),
                note.completed,
                note.category?.id,
                Priority.values().indexOf(note.priority),
                note.user.id
            )
        )
    }

    override fun getNotes(user: User): List<Note> {
        return db.noteDao().getAll(user.id).map {
            val category =
                if (it.categoryId != null) categoryRepository.getCategory(it.categoryId) else null
            Note(
                it.id,
                it.title,
                it.description,
                LocalDate.parse(it.creationDate),
                LocalDate.parse(it.completionDate),
                it.completed,
                if (category != null) Category(category.id, category.name, category.user) else null,
                Priority.values()[it.priority],
                userRepository.getUser(it.userId)
            )
        }
    }


    override fun getNote(id: Int): Note {
        val note = db.noteDao().getNote(id) ?: throw RuntimeException("Note not found")
        val category =
            if (note.categoryId != null) categoryRepository.getCategory(note.categoryId) else null
        val user = userRepository.getUser(note.userId)

        return Note(
            note.id,
            note.title, note.description,
            LocalDate.parse(note.creationDate),
            LocalDate.parse(note.completionDate),
            note.completed,
            category,
            Priority.values()[note.priority],
            user
        )
    }
}