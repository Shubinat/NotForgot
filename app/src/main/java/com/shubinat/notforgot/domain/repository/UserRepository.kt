package com.shubinat.notforgot.domain.repository

import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User

interface UserRepository {
    suspend fun getUser(id: Int) : User
    suspend fun authorizeUser(authData: LoginUser) : User?
    suspend fun registerUser(user: User)
}