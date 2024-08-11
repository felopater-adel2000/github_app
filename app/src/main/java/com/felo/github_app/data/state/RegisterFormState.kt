package com.felo.github_app.data.state

data class RegisterFormState(
    val isDataValid: Boolean = false,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isConfirmationPasswordValid: Boolean = true,
)
