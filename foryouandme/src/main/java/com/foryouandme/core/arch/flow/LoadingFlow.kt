package com.foryouandme.core.arch.flow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

data class UILoading<T>(val task: T, val active: Boolean)

class LoadingFlow<T> @Inject constructor() {

    private val loadingFlow: MutableSharedFlow<UILoading<T>> = MutableSharedFlow(replay = 1)
    val loading: SharedFlow<UILoading<T>>
        get() = loadingFlow


    suspend fun show(loading: T) {

        loadingFlow.emit(UILoading(loading, true))

    }

    suspend fun hide(loading: T) {

        loadingFlow.emit(UILoading(loading, false))

    }

}