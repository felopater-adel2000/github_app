package com.felo.github_app.preferences

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SessionManager(
    val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    fun isInternetAvailable(): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
/*
    fun isUserLogin(): Boolean =
        sharedPreferences.getBoolean(PreferenceKeys.USER_STATUS, false)

    fun setUserLoginStatus(status: Boolean) =
        sharedPreferences.edit().putBoolean(PreferenceKeys.USER_STATUS, status).apply()*/

    fun isUserLoggedIn(): Boolean = Firebase.auth.currentUser != null

}