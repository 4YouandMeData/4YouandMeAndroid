package com.foryouandme.core.arch.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class StateUpdateFlow<T> @Inject constructor() {

    private val stateUpdatesFlow: MutableSharedFlow<UIEvent<T>> = MutableSharedFlow(replay = 1)
    val stateUpdates: SharedFlow<UIEvent<T>>
        get() = stateUpdatesFlow


    suspend fun update(update: T) {

        stateUpdatesFlow.emit(update.toUIEvent())

    }

    @ExperimentalCoroutinesApi
    fun resetReplayCache() {

        stateUpdatesFlow.resetReplayCache()

    }

}