package com.shubinat.notforgot.domain.usecases.users

import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.repository.UserRepository

class RegisterUserUseCase(private val repository: UserRepository) {
    operator fun invoke(user: User) {
        repository.registerUser(user)
    }
}