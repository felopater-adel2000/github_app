package com.felo.github_app.utils

import com.felo.github_app.network.DataState
import com.felo.github_app.network.StateError

interface DataStateChangeListener {
    fun onDataStateChange(dataState: DataState<*>?)
    fun onErrorStateChange(stateError: StateError)
}