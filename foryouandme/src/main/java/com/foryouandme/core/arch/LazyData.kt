package com.foryouandme.core.arch

import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.ui.compose.error.toForYouAndMeException

sealed class LazyData<out T> {

    object Empty : LazyData<Nothing>()

    data class Data<out T>(val data: T) : LazyData<T>()

    object Loading : LazyData<Nothing>()

    data class Error(val error: ForYouAndMeException) : LazyData<Nothing>()

    fun <A> map(mapper: (T) -> A): LazyData<A> =
        when (this) {
            is Data -> mapper(data).toData()
            Empty -> Empty
            is Error -> this
            Loading -> Loading
        }

    fun orNull(): T? =
        when (this) {
            is Data -> data
            else -> null
        }

    fun isLoading(): Boolean = this is Loading

    companion object {

        fun unit(): LazyData.Data<Unit> = Unit.toData()

    }

}

fun <T> T.toData(): LazyData.Data<T> = LazyData.Data(this)

fun Throwable.toError(): LazyData.Error =
    LazyData.Error(this.toForYouAndMeException())

fun ForYouAndMeException.toError(): LazyData.Error = LazyData.Error(this)