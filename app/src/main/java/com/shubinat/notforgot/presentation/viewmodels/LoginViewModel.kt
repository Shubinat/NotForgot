package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shubinat.notforgot.data.local.repository.UserRepositoryImpl
import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.usecases.users.AuthorizeUserUseCase

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

    var successAuthorizationListener : SuccessAuthorizationListener? = null

    fun login() {

        if (login.get().isNullOrEmpty()) {
            _loginError.value = true
        }

        if (password.get().isNullOrEmpty()) {
            _passwordError.value = true
        }

        if (loginError.value == false && passwordError.value == false) {
            try {
                val user = authorizeUserUseCase(
                    LoginUser(login.get() ?: "", password.get() ?: "")
                )
                if (user != null) {
                    successAuthorizationListener?.successAuthorization(user)
                } else {
                    Toast.makeText(
                        getApplication(),
                        "Неверный логин или пароль",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (ex: Exception) {
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

    interface SuccessAuthorizationListener {
        fun successAuthorization(user: User)
    }
}