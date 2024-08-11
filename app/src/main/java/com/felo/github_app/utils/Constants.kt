package com.felo.github_app.utils

class Constants {
    companion object {
        const val BASE_URL = "https://api.github.com/"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "No Internet"
        const val NETWORK_TIMEOUT = 60000L
        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing
        const val ERROR_UNKNOWN = "Unknown error"
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
        val EXPECTED_INTERNET_ERROR_MESSAGES =
            listOf(UNABLE_TODO_OPERATION_WO_INTERNET, UNABLE_TO_RESOLVE_HOST)

        const val PAGINATION_PAGE_SIZE = 20
    }
}