package com.shubinat.notforgot.domain.usecases.users

import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.UserRepository

class AuthorizeUserUseCase(private val repository: UserRepository) {
    operator fun invoke(authData: LoginUser): User? {
        return repository.authorizeUser(authData)
    }
}