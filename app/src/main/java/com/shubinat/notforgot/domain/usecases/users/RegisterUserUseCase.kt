package com.shubinat.notforgot.domain.usecases.users

import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) {
        withContext(Dispatchers.IO) {
            repository.registerUser(user)
        }
    }
}