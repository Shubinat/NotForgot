package com.shubinat.notforgot.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shubinat.notforgot.data.room.dao.CategoryDao
import com.shubinat.notforgot.data.room.dao.NoteDao
import com.shubinat.notforgot.data.room.dao.UserDao
import com.shubinat.notforgot.data.room.entity.Category
import com.shubinat.notforgot.data.room.entity.Note
import com.shubinat.notforgot.data.room.entity.User

@Database(entities = [User::class, Category::class, Note::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            if (INSTANCE == null)
                 INSTANCE = Room.databaseBuilder(
                     context,
                     AppDatabase::class.java,
                     "not-forgot-database"
                 ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
             return INSTANCE as AppDatabase
        }

    }
}