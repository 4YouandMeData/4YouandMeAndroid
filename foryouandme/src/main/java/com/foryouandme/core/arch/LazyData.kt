package com.foryouandme.core.arch

import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.ui.compose.error.toForYouAndMeException

sealed class LazyData<out T> {

    object Empty : LazyData<Nothing>()

    data class Data<out T>(val value: T) : LazyData<T>()

    data class Loading<out T>(val value: T? = null) : LazyData<T>()

    data class Error<out T>(val error: ForYouAndMeException, val value: T? = null) : LazyData<T>()

    fun <A> map(mapper: (T) -> A): LazyData<A> =
        when (this) {
            is Data -> mapper(value).toData()
            Empty -> Empty
            is Error -> Error(error, value?.let { mapper(it) })
            is Loading -> Loading(value?.let { mapper(it) })
        }

    fun orNull(): T? =
        when (this) {
            is Data -> value
            else -> null
        }

    fun currentOrPrevious(): T? =
        when (this) {
            is Data -> value
            Empty -> null
            is Error -> value
            is Loading -> value
        }

    fun toLoading(): Loading<T> =
        when (this) {
            is Data -> Loading(value = value)
            Empty -> Loading(value = null)
            is Error -> Loading(value = value)
            is Loading -> Loading(value = value)
        }

    fun toError(error: Throwable): Error<T> =
        when (this) {
            is Data -> Error(error = error.toForYouAndMeException(), value = value)
            Empty -> Error(error = error.toForYouAndMeException(), value = null)
            is Error -> Error(error = error.toForYouAndMeException(), value = value)
            is Loading -> Error(error = error.toForYouAndMeException(), value = value)
        }

    fun isLoading(): Boolean = this is Loading

    fun isData(): Boolean = this is Data

    fun isError(): Boolean = this is Error

    companion object {

        fun unit(): Data<Unit> = Unit.toData()

    }

}

fun <T> T.toData(): LazyData.Data<T> = LazyData.Data(this)

fun <T> Throwable.toError(value: T? = null): LazyData.Error<T> =
    LazyData.Error(this.toForYouAndMeException(), value)

fun <T> ForYouAndMeException.toError(value: T? = null): LazyData.Error<T> =
    LazyData.Error(this, value)