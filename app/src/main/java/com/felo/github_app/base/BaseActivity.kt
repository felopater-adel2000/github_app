package com.felo.github_app.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.felo.github_app.network.DataState
import com.felo.github_app.network.Response
import com.felo.github_app.network.ResponseType
import com.felo.github_app.network.StateError
import com.felo.github_app.preferences.SessionManager
import com.felo.github_app.utils.DataStateChangeListener
import com.felo.github_app.utils.displayErrorDialog
import com.felo.github_app.utils.displaySuccessDialog
import com.felo.github_app.utils.displayToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity(), DataStateChangeListener {
    private val TAG = "BaseActivity"

    val sessionManager: SessionManager by inject()

    val requestManager: RequestManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun handleStateErrorEvent(stateError: StateError) {
        Log.d(TAG, "handleStateErrorEvent: ${stateError.response?.message}")
        when (stateError.response?.responseType) {
            is ResponseType.Dialog -> {
                stateError.response.message?.let { message -> displayErrorDialog(message) }
                stateError.response.localizedMessage?.let { message -> displayErrorDialog(message) }
            }

            is ResponseType.Toast -> {
                stateError.response.message?.let { message -> displayToast(message) }
            }

            else -> {
                Log.e(TAG, "handleStateErrorEvent: ${stateError.response?.message}")
            }


        }
    }

    private fun handleStateResponse(response: Response) {
        when (response.responseType) {
            is ResponseType.Dialog -> {
                response.message?.let { message -> displaySuccessDialog(message) }
                response.localizedMessage?.let { message -> displaySuccessDialog(message) }
            }

            is ResponseType.Toast -> {
                response.message?.let { message -> displayToast(message) }
            }

            else -> {
                Log.d(TAG, "handleStateErrorEvent: ${response.message}")
            }
        }
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        dataState?.let {
            lifecycleScope.launch(Dispatchers.Main) {
                showProgressBar(it.loading.isLoading)
                it.error?.let { errorEvent ->
                    handleStateErrorEvent(errorEvent)
                }
                it.data?.let {
                    it.response?.let { responseEvent ->
                        handleStateResponse(responseEvent)
                    }
                }
            }
        }
    }

    override fun onErrorStateChange(
        stateError: StateError
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            handleStateErrorEvent(stateError)
        }
    }

    abstract fun showProgressBar(show: Boolean)

    abstract fun showErrorUI(
        show: Boolean,
        image: Int? = 0,
        title: String? = "",
        desc: String? = null,
        isAuthenticated: Boolean? = true,
        showButton: Boolean? = false
    )
}