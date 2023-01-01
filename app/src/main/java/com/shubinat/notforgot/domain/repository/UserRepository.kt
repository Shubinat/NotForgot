package com.shubinat.notforgot.domain.repository

import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User

interface UserRepository {
    fun authorizeUser(authData: LoginUser) : User?
    fun registerUser(user: User)
}