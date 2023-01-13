package com.shubinat.notforgot.data.roomWithRetrofit.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shubinat.notforgot.data.roomWithRetrofit.dao.AccessTokenDao
import com.shubinat.notforgot.data.roomWithRetrofit.dao.CategoryDao
import com.shubinat.notforgot.data.roomWithRetrofit.dao.NoteDao
import com.shubinat.notforgot.data.roomWithRetrofit.dao.UserDao
import com.shubinat.notforgot.data.roomWithRetrofit.entities.AccessToken
import com.shubinat.notforgot.data.roomWithRetrofit.entities.Category
import com.shubinat.notforgot.data.roomWithRetrofit.entities.Note
import com.shubinat.notforgot.data.roomWithRetrofit.entities.User

@Database(
    entities = [User::class, Category::class, Note::class, AccessToken::class],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accessTokenDao(): AccessTokenDao
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null)
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "not-forgot.db"
                ).fallbackToDestructiveMigration().build()
            return INSTANCE as AppDatabase
        }

    }
}