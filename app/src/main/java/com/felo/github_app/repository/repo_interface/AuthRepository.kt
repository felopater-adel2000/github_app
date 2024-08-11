package com.felo.github_app.repository.repo_interface

import com.felo.github_app.network.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun register(
        email: String,
        password: String,
    ): StateFlow<DataState<Boolean>>

    fun login(
        email: String,
        password: String,
    ): StateFlow<DataState<Boolean>>

}