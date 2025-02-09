package com.felo.github_app.network

data class Loading(val isLoading: Boolean)
data class Data<T>(val data: T?, val response: Response?)
data class StateError(val response: Response?)

data class Response(
    val message: String? = null,
    val localizedMessage: Int? = 0,
    val responseType: ResponseType
)

sealed class ResponseType {
    class Toast : ResponseType()
    class Dialog : ResponseType()
    class SnakeBar : ResponseType()
    class None : ResponseType()
}

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
data class Event<T>(private val content: T) {

    var hasBeenHandled = false
    // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    override fun toString(): String {
        return "Event(content=$content,hasBeenHandled=$hasBeenHandled)"
    }

    companion object {

        // we don't want an event if there's no data
        fun <T> dataEvent(data: T?): Event<T>? {
            data?.let {
                return Event(it)
            }
            return null
        }

        // we don't want an event if there is no message
        fun responseEvent(response: Response?): Event<Response>? {
            response?.let {
                return Event(response)
            }
            return null
        }
    }


}