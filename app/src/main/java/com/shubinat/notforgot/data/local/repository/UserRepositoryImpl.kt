package com.shubinat.notforgot.data.local.repository

import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.UserRepository

object UserRepositoryImpl : UserRepository {

    private val users = mutableListOf(
        User(1, "Виталий", "1", "1"),
        User(2, "Иван", "2", "2"),
        User(3, "Владимир", "3", "3"),
        User(4, "Александр", "4", "4"),
        User(5, "Сергей", "5", "5"),
        User(6, "Максим", "6", "6"),
        User(7, "Елена", "7", "7"),
    )

    private var idCounter = 7
    override suspend fun getUser(id: Int): User {
        TODO("Not yet implemented")
    }

    override suspend fun authorizeUser(authData: LoginUser): User? {
        return users.firstOrNull {
            it.login == authData.login && it.password == authData.password
        }

    }

    override suspend fun registerUser(user: User) {
        if (user.name.isBlank()) throw RuntimeException("Name cannot be blank")
        if (user.login.isBlank()) throw RuntimeException("Login cannot be blank")
        if (user.password.isBlank()) throw RuntimeException("Password cannot be blank")
        val existUser = users.firstOrNull {
            it.login == user.login
        }

        if (user.id == 0) {
            user.id = ++idCounter
        } else {
            if (users.firstOrNull { it.id == user.id } != null)
                throw RuntimeException("User with id: ${user.id} is exist")
        }
        if (existUser != null) throw RuntimeException("User with login: ${user.login} is exist")
        users.add(user)
    }
}