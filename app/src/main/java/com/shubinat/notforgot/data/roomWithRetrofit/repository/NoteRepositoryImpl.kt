package com.shubinat.notforgot.data.roomWithRetrofit.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.GsonBuilder
import com.shubinat.notforgot.data.roomWithRetrofit.database.AppDatabase
import com.shubinat.notforgot.data.roomWithRetrofit.exceptions.UnauthorizedException
import com.shubinat.notforgot.data.roomWithRetrofit.models.NoteModel
import com.shubinat.notforgot.data.roomWithRetrofit.services.NetworkService
import com.shubinat.notforgot.data.roomWithRetrofit.utils.convertToSuspend
import com.shubinat.notforgot.data.roomWithRetrofit.workers.AddNoteWorker
import com.shubinat.notforgot.data.roomWithRetrofit.workers.EditNoteWorker
import com.shubinat.notforgot.domain.entity.*
import com.shubinat.notforgot.domain.repository.NoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class NoteRepositoryImpl private constructor(private val context: Context) : NoteRepository {


    private val db = AppDatabase.getInstance(context)
    private val service = NetworkService.getInstance()
    private val api = service.getNotForgotApi()
    private val userRepository = UserRepositoryImpl.getInstance(context)
    private val categoryRepository = CategoryRepositoryImpl.getInstance(context)
    private var firstLoad = true

    override suspend fun addNote(note: Note) {
        if (note.title.isBlank()) throw RuntimeException("Title cannot be blank")
        if (note.description.isBlank()) throw RuntimeException("Description cannot be blank")

        db.noteDao().addNote(
            com.shubinat.notforgot.data.roomWithRetrofit.entities.Note(
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
        addNoteBackground(note)
    }

    override suspend fun editNote(note: Note) {
        if (note.title.isBlank()) throw RuntimeException("Title cannot be blank")
        if (note.description.isBlank()) throw RuntimeException("Description cannot be blank")

        getNotes(note.user).firstOrNull { it.id == note.id }
            ?: throw RuntimeException("Note not found")

        db.noteDao().editNote(
            com.shubinat.notforgot.data.roomWithRetrofit.entities.Note(
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

        editNoteBackground(note)
    }

    override suspend fun getNotes(user: User): List<Note> {
        var notesList: List<Note>
        if (service.isOnline(context) && firstLoad) {
            try {
                val dbUser = db.userDao().findUser(user.id) ?: throw UnauthorizedException()
                val token = "Bearer " + dbUser.token
                notesList = loadNotesFromApi(token, user)
            } catch (ex: UnauthorizedException) {
                val loginUser = LoginUser(user.login, user.password)
                runBlocking {
                    userRepository.authorizeUser(loginUser)
                }
                notesList = getNotes(user)
            }
            firstLoad = false
        } else {
            notesList = loadNotesFromDb(user)
        }
        return notesList
    }

    override suspend fun getNote(id: Int): Note {
        val note = db.noteDao().getNote(id) ?: throw RuntimeException("Note not found: $id")
        val category = note.categoryId?.let {
            categoryRepository.getCategory(it)
        }
        val user = userRepository.getUser(note.userId)

        return Note(
            note.id,
            note.title,
            note.description,
            LocalDate.parse(note.creationDate),
            LocalDate.parse(note.completionDate),
            note.completed,
            category,
            Priority.values()[note.priority],
            user
        )
    }

    private suspend fun loadNotesFromApi(accessToken: String, user: User): List<Note> {
        val categories = categoryRepository.getCategories(user)


        val notesTask = coroutineScope {
            async {
                api.getAllNotes(accessToken).convertToSuspend()
            }
        }

        val notesList = notesTask.await().map {
            val categoryModel = categories.firstOrNull { c -> c.id == it.categoryId }
            var category: Category? = null
            categoryModel?.let { model ->
                category = Category(
                    model.id,
                    model.name,
                    user
                )
            }
            Note(
                it.id,
                it.title,
                it.description,
                LocalDate.parse(it.creationDate),
                LocalDate.parse(it.completionDate),
                it.completed,
                category,
                Priority.values()[it.priority],
                user
            )
        }
        saveNotesInDb(notesList)
        return notesList
    }

    private fun saveNotesInDb(notesList: List<Note>) {
        db.noteDao().clear()
        val notes = notesList.map {
            com.shubinat.notforgot.data.roomWithRetrofit.entities.Note(
                it.id,
                it.title,
                it.description,
                it.creationDate.toString(),
                it.completionDate.toString(),
                it.completed,
                it.category?.id,
                Priority.values().indexOf(it.priority),
                it.user.id
            )
        }

        for (note in notes) {
            db.noteDao().addNote(note)
        }
    }

    private suspend fun loadNotesFromDb(user: User): List<Note> {
        val categories = categoryRepository.getCategories(user)

        return db.noteDao().getAll(user.id).map {
            var category: Category? = null
            it.categoryId?.let { id ->
                    val categoryModel = categories.firstOrNull { category -> id == category.id }
                        ?: throw RuntimeException("Category not found")
                    category = Category(
                        categoryModel.id,
                        categoryModel.name,
                        user
                    )
            }
            Note(
                it.id,
                it.title,
                it.description,
                LocalDate.parse(it.creationDate),
                LocalDate.parse(it.completionDate),
                it.completed,
                category,
                Priority.values()[it.priority],
                user
            )
        }
    }

    private fun editNoteBackground(note: Note) {
        val gson = GsonBuilder().create()
        val model = NoteModel(
            note.id,
            note.title,
            note.description,
            note.creationDate.toString(),
            note.completionDate.toString(),
            note.completed,
            note.category?.id,
            Priority.values().indexOf(note.priority)
        )
        val json = gson.toJson(model)

        val data = Data.Builder()
            .putString(EditNoteWorker.NOTE_KEY, json)
            .putInt(EditNoteWorker.USER_ID_KEY, note.user.id)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<EditNoteWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val manager = WorkManager.getInstance(context)
        manager.enqueue(workRequest)
    }

    private fun addNoteBackground(note: Note) {
        val gson = GsonBuilder().create()
        val model = NoteModel(
            note.id,
            note.title,
            note.description,
            note.creationDate.toString(),
            note.completionDate.toString(),
            note.completed,
            note.category?.id,
            Priority.values().indexOf(note.priority)
        )
        val json = gson.toJson(model)

        val data = Data.Builder()
            .putString(EditNoteWorker.NOTE_KEY, json)
            .putInt(EditNoteWorker.USER_ID_KEY, note.user.id)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<AddNoteWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val manager = WorkManager.getInstance(context)
        manager.enqueue(workRequest)

    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: NoteRepositoryImpl? = null

        fun getInstance(context: Context): NoteRepositoryImpl {
            if (INSTANCE == null)
                INSTANCE = NoteRepositoryImpl(context)
            return INSTANCE as NoteRepositoryImpl
        }
    }

}