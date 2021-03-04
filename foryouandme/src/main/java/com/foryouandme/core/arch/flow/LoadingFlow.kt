package com.foryouandme.core.arch.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

data class UILoading<T>(val task: T, val active: Boolean)

class LoadingFlow<T> @Inject constructor() {

    private val loadingFlow: MutableSharedFlow<UILoading<UIEvent<T>>> =
            MutableSharedFlow(replay = 1)
    val loading: SharedFlow<UILoading<UIEvent<T>>>
        get() = loadingFlow


    suspend fun show(loading: T) {

        loadingFlow.emit(UILoading(loading.toUIEvent(), true))

    }

    suspend fun hide(loading: T) {

        loadingFlow.emit(UILoading(loading.toUIEvent(), false))

    }

}

fun <T> Flow<UILoading<UIEvent<T>>>.unwrapEvent(handlerId: String): Flow<UILoading<T>> =
        mapNotNull { uiLoading ->
            uiLoading.task.getContentByHandler(handlerId)
                    .getOrNull()
                    ?.let { UILoading(it, uiLoading.active) }
        }