package com.shubinat.notforgot.data.room.repository

import android.app.Application
import androidx.room.Room
import com.shubinat.notforgot.data.room.database.AppDatabase
import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.UserRepository


class UserRepositoryImpl(application: Application) : UserRepository {
    private val db : AppDatabase = AppDatabase.getInstance(application.applicationContext)
    override fun getUser(id: Int) : User {
        val user = db.userDao().findUser(id) ?: throw RuntimeException("User not found")
        return User(user.id, user.name, user.login, user.password)
    }

    override fun authorizeUser(authData: LoginUser): User? {
        val dbUser = db.userDao().findUser(authData.login, authData.password) ?: return null
        return User(
            dbUser.id,
            dbUser.name,
            dbUser.login,
            dbUser.password
        )
    }

    override fun registerUser(user: User) {
        val dbUser = com.shubinat.notforgot.data.room.entity.User(
            user.id,
            user.name,
            user.login,
            user.password
        )
        db.userDao().addUser(dbUser)
    }
}