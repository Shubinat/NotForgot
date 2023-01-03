package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shubinat.notforgot.R
import com.shubinat.notforgot.data.room.repository.UserRepositoryImpl
import com.shubinat.notforgot.domain.entity.LoginUser
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.usecases.users.AuthorizeUserUseCase
import kotlinx.coroutines.*

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val repository = UserRepositoryImpl(application)

    val authorizeUserUseCase = AuthorizeUserUseCase(repository)

    val login: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()

    private val _loginError = MutableLiveData<Boolean>()
    val loginError: LiveData<Boolean>
        get() = _loginError

    private val _passwordError = MutableLiveData<Boolean>()
    val passwordError: LiveData<Boolean>
        get() = _passwordError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    var successAuthorizationListener: SuccessAuthorizationListener? = null

    fun login() {

        if (login.get().isNullOrEmpty()) {
            _loginError.value = true
        }

        if (password.get().isNullOrEmpty()) {
            _passwordError.value = true
        }

        if (loginError.value == false && passwordError.value == false) {

            val operation = viewModelScope.async(Dispatchers.IO) {
                authorizeUserUseCase(
                    LoginUser(login.get() ?: "", password.get() ?: "")
                )
            }

            viewModelScope.launch(Dispatchers.Main) {
                delay(100)
                if (operation.isActive) {
                    try {
                        _loading.value = true

                        val user = operation.await()
                        onLogin(user)

                    } catch (ex: Exception) {
                        Toast.makeText(
                            getApplication(),
                            getApplication<Application>().getString(R.string.auth_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        _loading.value = false
                    }
                } else {
                    val user = operation.await()
                    onLogin(user)
                }
            }

        }
    }

    private fun onLogin(user: User?) {
        if (user != null) {
            successAuthorizationListener?.successAuthorization(user)
        } else {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.auth_invalid),
                Toast.LENGTH_SHORT
            )
                .show()
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