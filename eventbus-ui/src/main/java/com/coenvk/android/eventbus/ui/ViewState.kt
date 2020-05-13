package com.coenvk.android.eventbus.ui

import com.coenvk.android.eventbus.BusEvent

sealed class ViewState<out T, out E>(payload: T? = null) : BusEvent<T>(payload) {

    object Idle : ViewState<Nothing, Nothing>()
    object Loading : ViewState<Nothing, Nothing>()
    data class Success<out T>(val t: T) : ViewState<T, Nothing>(t)
    data class Error<out E>(val e: E) : ViewState<Nothing, E>()

    fun errorOrNull(): E? {
        return when (this) {
            is Error -> e
            else -> null
        }
    }

    fun getOrNull(): T? {
        return when (this) {
            is Success -> t
            else -> null
        }
    }

    fun ifError(block: (E) -> Unit): ViewState<T, E> =
        apply {
            if (this is Error) block(e)
        }

    fun ifSuccess(block: (T) -> Unit): ViewState<T, E> =
        apply {
            if (this is Success) block(t)
        }

}

fun <T, E : Throwable> ViewState<T, E>.throwIfError() {
    if (this is ViewState.Error) throw e
}