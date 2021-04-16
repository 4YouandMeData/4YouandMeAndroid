package com.foryouandme.core.arch.flow

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIError<T>(val cause: T, val error: Throwable)

class ErrorFlow<T> @Inject constructor() {

    private val errorFlow: MutableSharedFlow<UIError<UIEvent<T>>> = MutableSharedFlow(replay = 1)
    val error: SharedFlow<UIError<UIEvent<T>>>
        get() = errorFlow


    fun <A> launchCatch(scope: CoroutineScope, cause: T, block: suspend () -> A): Job =
            scope.launch(
                    CoroutineExceptionHandler { _, throwable ->
                        scope.launch { error(cause, throwable) }
                    }
            ) { block() }

    fun <A, L> launchCatch(
        scope: CoroutineScope,
        cause: T,
        loadingFlow: LoadingFlow<L>,
        loading: L,
        block: suspend () -> A): Job =
            launchCatch(scope, cause, loadingFlow, listOf(loading), block)

    fun <A, L> launchCatch(
        scope: CoroutineScope,
        cause: T,
        loadingFlow: LoadingFlow<L>,
        loading: List<L>,
        block: suspend () -> A): Job =
        scope.launch(
            CoroutineExceptionHandler { _, throwable ->
                scope.launch {
                    loading.forEach { loadingFlow.hide(it) }
                    error(cause, throwable)
                }
            }
        ) { block() }

    suspend fun error(cause: T, throwable: Throwable) {

        errorFlow.emit(UIError(cause.toUIEvent(), throwable))

    }

}

fun <T> Flow<UIError<UIEvent<T>>>.unwrapEvent(handlerId: String): Flow<UIError<T>> =
        mapNotNull { uiError ->
            uiError.cause.getContentByHandler(handlerId)
                    .getOrNull()
                    ?.let { UIError(it, uiError.error) }
        }