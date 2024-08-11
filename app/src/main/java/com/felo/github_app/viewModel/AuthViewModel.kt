package com.felo.github_app.viewModel

import androidx.lifecycle.ViewModel
import com.felo.github_app.data.state.RegisterFormState
import com.felo.github_app.repository.repo_interface.AuthRepository
import com.felo.github_app.utils.Validation
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    val registerFormState = MutableStateFlow(RegisterFormState())
    val loginFormState = MutableStateFlow(RegisterFormState())

    fun register(email: String, password: String) = repo.register(email, password)

    fun login(email: String, password: String) = repo.login(email, password)

    fun checkRegisterFormValidation(
        email: String,
        password: String,
        confirmationPassword: String,
    ) {
        if (!Validation.isValidEmail(email))
        {
            registerFormState.value = RegisterFormState(isEmailValid = false)
        }

        else if (!Validation.isValidPassword(password))
        {
            registerFormState.value = RegisterFormState(isPasswordValid = false)
        }
        else if (confirmationPassword != password)
        {
            registerFormState.value = RegisterFormState(isConfirmationPasswordValid = false)
        }
        else
        {
            registerFormState.value = RegisterFormState(isDataValid = true)
        }
    }

    fun checkLoginFormValidation(
        email: String,
        password: String,
    ) {
        if (!Validation.isValidEmail(email))
        {
            loginFormState.value = RegisterFormState(isEmailValid = false)
        }

        else if (!Validation.isValidPassword(password))
        {
            loginFormState.value = RegisterFormState(isPasswordValid = false)
        }
        else
        {
            loginFormState.value = RegisterFormState(isDataValid = true)
        }
    }
}