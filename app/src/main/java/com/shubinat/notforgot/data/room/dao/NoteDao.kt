package com.shubinat.notforgot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shubinat.notforgot.data.room.entity.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note WHERE user_id = :userId")
    fun getAll(userId: Int) : List<Note>

    @Query("SELECT * FROM Note WHERE id = :noteId LIMIT 1")
    fun getNote(noteId: Int) : Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun editNote(note: Note)
}