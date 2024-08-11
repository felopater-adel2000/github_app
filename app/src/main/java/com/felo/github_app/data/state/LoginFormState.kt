package com.felo.github_app.data.state

data class LoginFormState(
    val isDataValid: Boolean = false,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true
)