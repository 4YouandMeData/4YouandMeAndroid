package com.foryouandme.core.arch.flow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class StateUpdateFlow<T> @Inject constructor() {

    private val stateUpdatesFlow: MutableSharedFlow<T> = MutableSharedFlow()
    val stateUpdates: SharedFlow<T>
        get() = stateUpdatesFlow


    suspend fun update(update: T) {

        stateUpdatesFlow.emit(update)

    }

}