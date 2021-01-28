package com.foryouandme.core.arch.flow

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIError<T>(val cause: T, val error: Throwable)

class ErrorFlow<T> @Inject constructor() {

    private val errorFlow: MutableSharedFlow<UIError<T>> = MutableSharedFlow()
    val error: SharedFlow<UIError<T>>
        get() = errorFlow


    fun <A> launchCatch(scope: CoroutineScope, cause: T, block: suspend () -> A): Job =
            scope.launch(
                    CoroutineExceptionHandler { _, throwable ->
                        scope.launch { error(cause, throwable) }
                    }
            ) { block() }

    suspend fun error(cause: T, throwable: Throwable) {

        errorFlow.emit(UIError(cause, throwable))

    }

}