package com.shubinat.notforgot.data.roomWithRetrofit.repository

import android.annotation.SuppressLint
import android.content.Context
import com.shubinat.notforgot.data.roomWithRetrofit.database.AppDatabase
import com.shubinat.notforgot.data.roomWithRetrofit.models.LoginRequest
import com.shubinat.notforgot.data.roomWithRetrofit.models.RegisterRequest
import com.shubinat.notforgot.data.roomWithRetrofit.services.NetworkService
import com.shubinat.notforgot.data.roomWithRetrofit.utils.convertToSuspend
import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.UserRepository
import kotlinx.coroutines.*

class UserRepositoryImpl private constructor(private val context: Context) : UserRepository {

    private val service = NetworkService.getInstance()
    private val api = service.getNotForgotApi()
    private val db = AppDatabase.getInstance(context)

    override suspend fun getUser(id: Int): User {
        val dbUser = db.userDao().findUser(id) ?: throw RuntimeException("User not found")
        return User(dbUser.id, dbUser.name, dbUser.login, dbUser.password)
    }

    override suspend fun authorizeUser(authData: LoginUser): User? {
        if (service.isOnline(context)) {
            val result = api.authUser(LoginRequest(authData.login, authData.password))

            val operation = coroutineScope {
                async {
                    result.convertToSuspend()
                }
            }

            val response = operation.await()
            db.userDao().addUser(
                com.shubinat.notforgot.data.roomWithRetrofit.entities.User(
                    response.user.id,
                    response.user.name,
                    response.user.login,
                    response.user.password,
                    response.accessToken
                )
            )
            return User(
                response.user.id,
                response.user.name,
                response.user.login,
                response.user.password
            )
        } else {
            val user = db.userDao().findUser(authData.login, authData.password) ?: return null
            return User(user.id, user.name, user.login, user.password)
        }
    }

    override suspend fun registerUser(user: User) {
        if (user.name.isBlank()) throw RuntimeException("Name cannot be blank")
        if (user.login.isBlank()) throw RuntimeException("Login cannot be blank")
        if (user.password.isBlank()) throw RuntimeException("Password cannot be blank")

        val result = api.registerUser(RegisterRequest(user.name, user.login, user.password))


        val operation = coroutineScope {
            async {
                result.convertToSuspend()
            }
        }

        val userModel = operation.await()
        db.userDao().addUser(
            com.shubinat.notforgot.data.roomWithRetrofit.entities.User(
                userModel.id,
                userModel.name,
                userModel.login,
                userModel.password,
                null
            )
        )

    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: UserRepositoryImpl? = null

        fun getInstance(context: Context): UserRepositoryImpl {
            if (INSTANCE == null)
                INSTANCE = UserRepositoryImpl(context)
            return INSTANCE as UserRepositoryImpl
        }
    }
}

