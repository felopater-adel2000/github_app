package com.felo.github_app.network

import android.util.Log
import com.felo.github_app.utils.Constants.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.felo.github_app.utils.Constants.Companion.ERROR_UNKNOWN
import com.felo.github_app.utils.Constants.Companion.NETWORK_TIMEOUT
import com.felo.github_app.utils.Constants.Companion.TESTING_CACHE_DELAY
import com.felo.github_app.utils.Constants.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import com.felo.github_app.utils.Constants.Companion.UNABLE_TO_RESOLVE_HOST
import com.google.gson.Gson
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class NetworkBoundResource<ResponseObject, CacheObject>(
    isNetworkAvailable: Boolean,//is there a network connection?
    isNetworkRequest: Boolean,//is this a network request?
    shouldLoadFromCache: Boolean,//should the cached data loaded firstly?
    shouldCancelIfNoInternet: Boolean//should cancel if there is no internet
) {

    private val TAG = "NetworkBoundResource"

    protected val result: MutableStateFlow<DataState<CacheObject>> = MutableStateFlow(DataState.Loading(isLoading = false))
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())

        if (isNetworkRequest) {
            setValue(DataState.Loading(isLoading = true))
        }

        if (shouldLoadFromCache) {
            coroutineScope.launch {
                loadFromCache().collect {
                    setValue(DataState.Loading(isLoading = true, cashedData = it))
                }
            }
        }

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                doNetworkRequest()
            } else {
                if (shouldCancelIfNoInternet) {
                    onErrorReturn(
                        UNABLE_TODO_OPERATION_WO_INTERNET,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                } else {
                    doCacheRequest()
                }
            }
        } else {
            doCacheRequest()
        }
    }

    private fun setValue(dataState: DataState<CacheObject>) {
        coroutineScope.launch {
            result.emit(dataState)
        }
    }

    private fun doNetworkRequest() {
        coroutineScope.launch {
            createCall().collect { response ->
                handleNetworkCall(response)
            }
        }

        coroutineScope.launch {
            delay(NETWORK_TIMEOUT)
            if (!job.isCompleted) {
                Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT.")
                job.cancel(CancellationException(UNABLE_TO_RESOLVE_HOST))
            }
        }
    }

    private suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>) {
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }

            is ApiErrorResponse -> {
                Log.e(TAG, "NetworkBoundResource: ${response.errorMessage}")
                onErrorReturn(
                    response.errorMessage,
                    shouldUseDialog = true,
                    shouldUseToast = false
                )
                /*try {
                    val errorBody =
                        Gson().fromJson(response.errorMessage, GenericResponse::class.java)
                    onErrorReturn(errorBody.message, shouldUseDialog = true, shouldUseToast = false)
                } catch (e: Exception)
                {
                    onErrorReturn(
                        response.errorMessage,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }*/
            }

            is ApiEmptyResponse -> {
                Log.e(TAG, "NetworkBoundResource: Request returned NOTHING (HTTP 204)")
                onErrorReturn(
                    "HTTP 204. Returned nothing.",
                    shouldUseDialog = true,
                    shouldUseToast = false
                )
            }
        }
    }

    private fun onErrorReturn(
        errorMessage: String?,
        shouldUseDialog: Boolean,
        shouldUseToast: Boolean
    ){
        var msg = errorMessage
        var responseType: ResponseType = ResponseType.None()

        if (msg == null) {
            msg = ERROR_UNKNOWN
        } else if (isNetworkError(msg)) {
            msg = ERROR_CHECK_NETWORK_CONNECTION
        }

        if (shouldUseToast) {
            responseType = ResponseType.Toast()
        } else if (shouldUseDialog) {
            responseType = ResponseType.Dialog()
        }

        onCompleteJob(
            DataState.Error(
                response = Response(
                    message = msg,
                    responseType = responseType
                )
            )
        )
    }

    fun onCompleteJob(dataState: DataState<CacheObject>) {
        coroutineScope.launch {
            job.complete()
            result.emit(dataState)
        }
    }

    private fun doCacheRequest() {
        coroutineScope.launch {
            //fake delay
            delay(TESTING_CACHE_DELAY)

            //View data from cache and return
            createCacheRequest()
        }
    }

    private fun isNetworkError(msg: String): Boolean {
        when {
            msg.contains(UNABLE_TO_RESOLVE_HOST) -> return true
            else -> return false
        }
    }

    private fun initNewJob(): Job {
        Log.d(TAG, "initNewJob: called")

        job = Job()
        job.invokeOnCompletion {
            if (job.isCancelled) {
                it?.let {
                    //show error dialog
                    onErrorReturn(it.message, shouldUseDialog = false, shouldUseToast = false)
                } ?: onErrorReturn(ERROR_UNKNOWN, shouldUseDialog = false, shouldUseToast = false)
            } else if (job.isCompleted) {
                Log.d(TAG, "invoke: Job has been completed")
                //Do Nothing, should be handled already
            }
        }
        coroutineScope = CoroutineScope(Dispatchers.IO + job)
        return job
    }


    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)
    abstract suspend fun createCacheRequest()
    abstract fun createCall(): Flow<GenericApiResponse<ResponseObject>>
    abstract fun setJob(job: Job)
    abstract fun loadFromCache(): StateFlow<CacheObject?>
    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)

    fun asStateFlow(): StateFlow<DataState<CacheObject>> = result

}