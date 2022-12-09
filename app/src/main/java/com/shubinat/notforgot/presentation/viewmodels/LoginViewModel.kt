package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.shubinat.notforgot.data.local.repository.UserRepositoryImpl
import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.usecases.users.AuthorizeUserUseCase
import com.shubinat.notforgot.domain.usecases.users.RegisterUserUseCase

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val repository = UserRepositoryImpl

    val authorizeUserUseCase = AuthorizeUserUseCase(repository)
    val registerUserUseCase = RegisterUserUseCase(repository)

    val login: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()

    fun onLogin() {
        authorizeUserUseCase(
            LoginUser(login.get() ?: "", password.get() ?: "")
        )
    }
}