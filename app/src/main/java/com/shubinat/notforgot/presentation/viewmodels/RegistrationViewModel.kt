package com.shubinat.notforgot.presentation.viewmodels

import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.shubinat.notforgot.data.local.repository.UserRepositoryImpl
import com.shubinat.notforgot.domain.entity.User
import com.shubinat.notforgot.domain.usecases.users.AuthorizeUserUseCase
import com.shubinat.notforgot.domain.usecases.users.RegisterUserUseCase
import com.shubinat.notforgot.presentation.fragments.LoginFragmentDirections

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    interface SuccessRegistrationListener {
        fun successRegistration()
    }

    val repository = UserRepositoryImpl

    val registerUserUseCase = RegisterUserUseCase(repository)

    val userName: ObservableField<String> = ObservableField()
    val login: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()
    val retryPassword: ObservableField<String> = ObservableField()

    private val _userNameError = MutableLiveData<Boolean>()
    val userNameError: LiveData<Boolean>
        get() = _userNameError

    private val _loginError = MutableLiveData<Boolean>()
    val loginError: LiveData<Boolean>
        get() = _loginError

    private val _passwordError = MutableLiveData<Boolean>()
    val passwordError: LiveData<Boolean>
        get() = _passwordError

    private val _retryPasswordError = MutableLiveData<Boolean>()
    val retryPasswordError: LiveData<Boolean>
        get() = _retryPasswordError

    var successRegistrationListener: SuccessRegistrationListener? = null


    fun register() {

        if (userName.get().isNullOrEmpty()) {
            _userNameError.value = true
        }

        if (login.get().isNullOrEmpty()) {
            _loginError.value = true
        }

        if (password.get().isNullOrEmpty()) {
            _passwordError.value = true
        }

        if (retryPassword.get() != password.get()) {
            _retryPasswordError.value = true
        }
        try {
            if (userNameError.value == false &&
                loginError.value == false &&
                passwordError.value == false &&
                retryPasswordError.value == false
            ) {
                registerUserUseCase(
                    User(
                        0,
                        userName.get() ?: "",
                        login.get() ?: "",
                        password.get() ?: ""
                    )
                )
                successRegistrationListener?.successRegistration()
            }

        }
        catch (ex: RuntimeException) {
            Toast.makeText(getApplication(),
                "Пользователь с данным логином уже существует",
                Toast.LENGTH_SHORT).show()
        }
        catch (ex: Exception) {
            Toast.makeText(getApplication(),
                "Ошибка регистрации",
                Toast.LENGTH_SHORT).show()
        }

    }

    fun resetUserNameError() {
        _userNameError.value = false
    }

    fun resetLoginError() {
        _loginError.value = false
    }

    fun resetPasswordError() {
        _passwordError.value = false
    }

    fun resetRetryPasswordError() {
        _retryPasswordError.value = false
    }
}