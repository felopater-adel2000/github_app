package com.felo.github_app.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class FlowCallAdapter <R> (private val responseType: Type)
    : CallAdapter<R, Flow<GenericApiResponse<R>>> {
    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): Flow<GenericApiResponse<R>> {
        return callbackFlow {
            var started = AtomicBoolean(false)

            if (started.compareAndSet(false, true))
            {
                call.enqueue(object: Callback<R> {
                    override fun onResponse(p0: Call<R>, p1: Response<R>) {
                        trySend(GenericApiResponse.create(p1)).isSuccess
                    }

                    override fun onFailure(p0: Call<R>, p1: Throwable) {
                        trySend(GenericApiResponse.create(p1)).isSuccess
                    }

                })

                awaitClose { call.cancel() }
            }
        }
    }

}