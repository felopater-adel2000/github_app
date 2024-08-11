package com.felo.github_app.repository.repo_impl

import android.util.Log
import com.felo.github_app.network.ApiInterface
import com.felo.github_app.network.Data
import com.felo.github_app.network.DataState
import com.felo.github_app.network.JobManager
import com.felo.github_app.network.Response
import com.felo.github_app.network.ResponseType
import com.felo.github_app.preferences.SessionManager
import com.felo.github_app.repository.repo_interface.AuthRepository
import com.felo.github_app.utils.orDefault
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class AuthRepositoryImpl(
    val sessionManager: SessionManager
) : JobManager("AuthRepository"), AuthRepository {
    private val TAG = "AuthRepositoryImpl"
    override fun register(email: String, password: String): StateFlow<DataState<Boolean>> {
        return callbackFlow<DataState<Boolean>> {

            if(sessionManager.isInternetAvailable())
            {
                Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful)
                    {
                        trySend(DataState.Success(true))
                    }
                    else
                    {
                        trySend(DataState.Error(Response(
                            message = task.exception?.message.orDefault(),
                            responseType = ResponseType.Toast()
                        )))
                    }

                }
            }
            else
            {
                trySend(DataState.Error(Response(
                    message = "No Internet Connection",
                    responseType = ResponseType.Toast()
                )))
            }

            awaitClose {  }
        }.stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, DataState.Loading(true))
    }

    override fun login(email: String, password: String): StateFlow<DataState<Boolean>> {
        return callbackFlow<DataState<Boolean>> {

            if (sessionManager.isInternetAvailable())
            {
                Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful)
                    {
                        trySend(DataState.Success(true))
                    }
                    else
                    {
                        Log.d(TAG, "login: ${task.exception?.message}")
                        trySend(DataState.Error(Response(
                            message = task.exception?.message,
                            responseType = ResponseType.Toast()
                        )))
                    }

                }
            }
            else
            {
                trySend(DataState.Error(Response(
                    message = "No Internet Connection",
                    responseType = ResponseType.Toast()
                )))
            }

            awaitClose {  }
        }.stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, DataState.Loading(true))
    }

}