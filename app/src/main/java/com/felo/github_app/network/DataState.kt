package com.felo.github_app.network

sealed class DataState<T>(
    var error: StateError? = null,
    var loading: com.felo.github_app.network.Loading = com.felo.github_app.network.Loading(false),
    var data: Data<T>? = null,
    var statusCode: Int? = null
) {

    class Success<T>(
        data: T? = null,
        response: Response? = null,
    ) : DataState<T>(
        data = Data(data, response),
        loading =com.felo.github_app.network.Loading(false)
    )

    class Error<T>(
        response: Response?,
        statusCode: Int? = null
    ) : DataState<T>(
        error = StateError(response),
        loading = com.felo.github_app.network.Loading(false),
        statusCode = statusCode
    )

    class Loading<T>(
        isLoading: Boolean,
        cashedData: T? = null
    ) : DataState<T>(
        loading = com.felo.github_app.network.Loading(isLoading),
        data = Data(cashedData, null)
    )

}