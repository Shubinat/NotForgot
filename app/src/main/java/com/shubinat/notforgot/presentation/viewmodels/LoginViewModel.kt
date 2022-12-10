package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shubinat.notforgot.data.local.repository.UserRepositoryImpl
import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.usecases.users.AuthorizeUserUseCase
import com.shubinat.notforgot.domain.usecases.users.RegisterUserUseCase

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val repository = UserRepositoryImpl

    val authorizeUserUseCase = AuthorizeUserUseCase(repository)

    val login: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()

    private val _loginError = MutableLiveData<Boolean>()
    val loginError: LiveData<Boolean>
        get() = _loginError

    private val _passwordError = MutableLiveData<Boolean>()
    val passwordError: LiveData<Boolean>
        get() = _passwordError

    fun login() {

        if (login.get().isNullOrEmpty()) {
            _loginError.value = true
        }

        if (password.get().isNullOrEmpty()) {
            _passwordError.value = true
        }

        if (loginError.value == false && passwordError.value== false){
            try {
                authorizeUserUseCase(
                    LoginUser(login.get() ?: "", password.get() ?: "")
                )
            } catch (ex: RuntimeException) {
                Toast.makeText(getApplication(), "Произошла ошибка авторизации", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    fun resetLoginError() {
        _loginError.value = false
    }

    fun resetPasswordError() {
        _passwordError.value = false
    }
}