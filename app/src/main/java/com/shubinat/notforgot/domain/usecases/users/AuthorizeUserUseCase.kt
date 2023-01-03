package com.shubinat.notforgot.domain.usecases.users

import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AuthorizeUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(authData: LoginUser): User? {
        return withContext(Dispatchers.IO) {
            repository.authorizeUser(authData)
        }
    }
}