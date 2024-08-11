package com.felo.github_app.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class AbsentStateFlow<T> private constructor() : StateFlow<T?> {
    override val replayCache: List<T?>
        get() = emptyList()
    override val value: T?
        get() = null

    override suspend fun collect(collector: FlowCollector<T?>): Nothing {
        MutableStateFlow(null).collect {

        }
    }


    companion object {
        fun <T> create(): StateFlow<T?>
        {
            return flow<T> {  }
                .stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Lazily, null)
        }
    }

}